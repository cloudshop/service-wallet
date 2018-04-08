package com.eyun.wallet.web.rest;

import com.eyun.wallet.WalletApp;

import com.eyun.wallet.config.SecurityBeanOverrideConfiguration;

import com.eyun.wallet.domain.WalletDetails;
import com.eyun.wallet.domain.Wallet;
import com.eyun.wallet.repository.WalletDetailsRepository;
import com.eyun.wallet.service.WalletDetailsService;
import com.eyun.wallet.service.dto.WalletDetailsDTO;
import com.eyun.wallet.service.mapper.WalletDetailsMapper;
import com.eyun.wallet.web.rest.errors.ExceptionTranslator;
import com.eyun.wallet.service.dto.WalletDetailsCriteria;
import com.eyun.wallet.service.WalletDetailsQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.eyun.wallet.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the WalletDetailsResource REST controller.
 *
 * @see WalletDetailsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WalletApp.class, SecurityBeanOverrideConfiguration.class})
public class WalletDetailsResourceIntTest {

    private static final Long DEFAULT_USERID = 1L;
    private static final Long UPDATED_USERID = 2L;

    private static final Long DEFAULT_AMOUNT = 1L;
    private static final Long UPDATED_AMOUNT = 2L;

    private static final Integer DEFAULT_TYPE = 1;
    private static final Integer UPDATED_TYPE = 2;

    private static final Instant DEFAULT_CREATED_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_BALANCE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TICKET = new BigDecimal(1);
    private static final BigDecimal UPDATED_TICKET = new BigDecimal(2);

    private static final BigDecimal DEFAULT_INTEGRAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_INTEGRAL = new BigDecimal(2);

    private static final BigDecimal DEFAULT_PAY_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PAY_PRICE = new BigDecimal(2);

    private static final String DEFAULT_ORDER_NO = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_NO = "BBBBBBBBBB";

    @Autowired
    private WalletDetailsRepository walletDetailsRepository;

    @Autowired
    private WalletDetailsMapper walletDetailsMapper;

    @Autowired
    private WalletDetailsService walletDetailsService;

    @Autowired
    private WalletDetailsQueryService walletDetailsQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWalletDetailsMockMvc;

    private WalletDetails walletDetails;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WalletDetailsResource walletDetailsResource = new WalletDetailsResource(walletDetailsService, walletDetailsQueryService);
        this.restWalletDetailsMockMvc = MockMvcBuilders.standaloneSetup(walletDetailsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WalletDetails createEntity(EntityManager em) {
        WalletDetails walletDetails = new WalletDetails()
            .userid(DEFAULT_USERID)
            .amount(DEFAULT_AMOUNT)
            .type(DEFAULT_TYPE)
            .createdTime(DEFAULT_CREATED_TIME)
            .balance(DEFAULT_BALANCE)
            .ticket(DEFAULT_TICKET)
            .integral(DEFAULT_INTEGRAL)
            .pay_price(DEFAULT_PAY_PRICE)
            .orderNo(DEFAULT_ORDER_NO);
        return walletDetails;
    }

    @Before
    public void initTest() {
        walletDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createWalletDetails() throws Exception {
        int databaseSizeBeforeCreate = walletDetailsRepository.findAll().size();

        // Create the WalletDetails
        WalletDetailsDTO walletDetailsDTO = walletDetailsMapper.toDto(walletDetails);
        restWalletDetailsMockMvc.perform(post("/api/wallet-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(walletDetailsDTO)))
            .andExpect(status().isCreated());

        // Validate the WalletDetails in the database
        List<WalletDetails> walletDetailsList = walletDetailsRepository.findAll();
        assertThat(walletDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        WalletDetails testWalletDetails = walletDetailsList.get(walletDetailsList.size() - 1);
        assertThat(testWalletDetails.getUserid()).isEqualTo(DEFAULT_USERID);
        assertThat(testWalletDetails.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testWalletDetails.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testWalletDetails.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testWalletDetails.getBalance()).isEqualTo(DEFAULT_BALANCE);
        assertThat(testWalletDetails.getTicket()).isEqualTo(DEFAULT_TICKET);
        assertThat(testWalletDetails.getIntegral()).isEqualTo(DEFAULT_INTEGRAL);
        assertThat(testWalletDetails.getPay_price()).isEqualTo(DEFAULT_PAY_PRICE);
        assertThat(testWalletDetails.getOrderNo()).isEqualTo(DEFAULT_ORDER_NO);
    }

    @Test
    @Transactional
    public void createWalletDetailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = walletDetailsRepository.findAll().size();

        // Create the WalletDetails with an existing ID
        walletDetails.setId(1L);
        WalletDetailsDTO walletDetailsDTO = walletDetailsMapper.toDto(walletDetails);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWalletDetailsMockMvc.perform(post("/api/wallet-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(walletDetailsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WalletDetails in the database
        List<WalletDetails> walletDetailsList = walletDetailsRepository.findAll();
        assertThat(walletDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkUseridIsRequired() throws Exception {
        int databaseSizeBeforeTest = walletDetailsRepository.findAll().size();
        // set the field null
        walletDetails.setUserid(null);

        // Create the WalletDetails, which fails.
        WalletDetailsDTO walletDetailsDTO = walletDetailsMapper.toDto(walletDetails);

        restWalletDetailsMockMvc.perform(post("/api/wallet-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(walletDetailsDTO)))
            .andExpect(status().isBadRequest());

        List<WalletDetails> walletDetailsList = walletDetailsRepository.findAll();
        assertThat(walletDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWalletDetails() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList
        restWalletDetailsMockMvc.perform(get("/api/wallet-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(walletDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].userid").value(hasItem(DEFAULT_USERID.intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(DEFAULT_CREATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.intValue())))
            .andExpect(jsonPath("$.[*].ticket").value(hasItem(DEFAULT_TICKET.intValue())))
            .andExpect(jsonPath("$.[*].integral").value(hasItem(DEFAULT_INTEGRAL.intValue())))
            .andExpect(jsonPath("$.[*].pay_price").value(hasItem(DEFAULT_PAY_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].orderNo").value(hasItem(DEFAULT_ORDER_NO.toString())));
    }

    @Test
    @Transactional
    public void getWalletDetails() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get the walletDetails
        restWalletDetailsMockMvc.perform(get("/api/wallet-details/{id}", walletDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(walletDetails.getId().intValue()))
            .andExpect(jsonPath("$.userid").value(DEFAULT_USERID.intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.createdTime").value(DEFAULT_CREATED_TIME.toString()))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.intValue()))
            .andExpect(jsonPath("$.ticket").value(DEFAULT_TICKET.intValue()))
            .andExpect(jsonPath("$.integral").value(DEFAULT_INTEGRAL.intValue()))
            .andExpect(jsonPath("$.pay_price").value(DEFAULT_PAY_PRICE.intValue()))
            .andExpect(jsonPath("$.orderNo").value(DEFAULT_ORDER_NO.toString()));
    }

    @Test
    @Transactional
    public void getAllWalletDetailsByUseridIsEqualToSomething() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where userid equals to DEFAULT_USERID
        defaultWalletDetailsShouldBeFound("userid.equals=" + DEFAULT_USERID);

        // Get all the walletDetailsList where userid equals to UPDATED_USERID
        defaultWalletDetailsShouldNotBeFound("userid.equals=" + UPDATED_USERID);
    }

    @Test
    @Transactional
    public void getAllWalletDetailsByUseridIsInShouldWork() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where userid in DEFAULT_USERID or UPDATED_USERID
        defaultWalletDetailsShouldBeFound("userid.in=" + DEFAULT_USERID + "," + UPDATED_USERID);

        // Get all the walletDetailsList where userid equals to UPDATED_USERID
        defaultWalletDetailsShouldNotBeFound("userid.in=" + UPDATED_USERID);
    }

    @Test
    @Transactional
    public void getAllWalletDetailsByUseridIsNullOrNotNull() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where userid is not null
        defaultWalletDetailsShouldBeFound("userid.specified=true");

        // Get all the walletDetailsList where userid is null
        defaultWalletDetailsShouldNotBeFound("userid.specified=false");
    }

    @Test
    @Transactional
    public void getAllWalletDetailsByUseridIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where userid greater than or equals to DEFAULT_USERID
        defaultWalletDetailsShouldBeFound("userid.greaterOrEqualThan=" + DEFAULT_USERID);

        // Get all the walletDetailsList where userid greater than or equals to UPDATED_USERID
        defaultWalletDetailsShouldNotBeFound("userid.greaterOrEqualThan=" + UPDATED_USERID);
    }

    @Test
    @Transactional
    public void getAllWalletDetailsByUseridIsLessThanSomething() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where userid less than or equals to DEFAULT_USERID
        defaultWalletDetailsShouldNotBeFound("userid.lessThan=" + DEFAULT_USERID);

        // Get all the walletDetailsList where userid less than or equals to UPDATED_USERID
        defaultWalletDetailsShouldBeFound("userid.lessThan=" + UPDATED_USERID);
    }


    @Test
    @Transactional
    public void getAllWalletDetailsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where amount equals to DEFAULT_AMOUNT
        defaultWalletDetailsShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the walletDetailsList where amount equals to UPDATED_AMOUNT
        defaultWalletDetailsShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllWalletDetailsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultWalletDetailsShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the walletDetailsList where amount equals to UPDATED_AMOUNT
        defaultWalletDetailsShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllWalletDetailsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where amount is not null
        defaultWalletDetailsShouldBeFound("amount.specified=true");

        // Get all the walletDetailsList where amount is null
        defaultWalletDetailsShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    public void getAllWalletDetailsByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where amount greater than or equals to DEFAULT_AMOUNT
        defaultWalletDetailsShouldBeFound("amount.greaterOrEqualThan=" + DEFAULT_AMOUNT);

        // Get all the walletDetailsList where amount greater than or equals to UPDATED_AMOUNT
        defaultWalletDetailsShouldNotBeFound("amount.greaterOrEqualThan=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllWalletDetailsByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where amount less than or equals to DEFAULT_AMOUNT
        defaultWalletDetailsShouldNotBeFound("amount.lessThan=" + DEFAULT_AMOUNT);

        // Get all the walletDetailsList where amount less than or equals to UPDATED_AMOUNT
        defaultWalletDetailsShouldBeFound("amount.lessThan=" + UPDATED_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllWalletDetailsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where type equals to DEFAULT_TYPE
        defaultWalletDetailsShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the walletDetailsList where type equals to UPDATED_TYPE
        defaultWalletDetailsShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllWalletDetailsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultWalletDetailsShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the walletDetailsList where type equals to UPDATED_TYPE
        defaultWalletDetailsShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllWalletDetailsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where type is not null
        defaultWalletDetailsShouldBeFound("type.specified=true");

        // Get all the walletDetailsList where type is null
        defaultWalletDetailsShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllWalletDetailsByTypeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where type greater than or equals to DEFAULT_TYPE
        defaultWalletDetailsShouldBeFound("type.greaterOrEqualThan=" + DEFAULT_TYPE);

        // Get all the walletDetailsList where type greater than or equals to UPDATED_TYPE
        defaultWalletDetailsShouldNotBeFound("type.greaterOrEqualThan=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllWalletDetailsByTypeIsLessThanSomething() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where type less than or equals to DEFAULT_TYPE
        defaultWalletDetailsShouldNotBeFound("type.lessThan=" + DEFAULT_TYPE);

        // Get all the walletDetailsList where type less than or equals to UPDATED_TYPE
        defaultWalletDetailsShouldBeFound("type.lessThan=" + UPDATED_TYPE);
    }


    @Test
    @Transactional
    public void getAllWalletDetailsByCreatedTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where createdTime equals to DEFAULT_CREATED_TIME
        defaultWalletDetailsShouldBeFound("createdTime.equals=" + DEFAULT_CREATED_TIME);

        // Get all the walletDetailsList where createdTime equals to UPDATED_CREATED_TIME
        defaultWalletDetailsShouldNotBeFound("createdTime.equals=" + UPDATED_CREATED_TIME);
    }

    @Test
    @Transactional
    public void getAllWalletDetailsByCreatedTimeIsInShouldWork() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where createdTime in DEFAULT_CREATED_TIME or UPDATED_CREATED_TIME
        defaultWalletDetailsShouldBeFound("createdTime.in=" + DEFAULT_CREATED_TIME + "," + UPDATED_CREATED_TIME);

        // Get all the walletDetailsList where createdTime equals to UPDATED_CREATED_TIME
        defaultWalletDetailsShouldNotBeFound("createdTime.in=" + UPDATED_CREATED_TIME);
    }

    @Test
    @Transactional
    public void getAllWalletDetailsByCreatedTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where createdTime is not null
        defaultWalletDetailsShouldBeFound("createdTime.specified=true");

        // Get all the walletDetailsList where createdTime is null
        defaultWalletDetailsShouldNotBeFound("createdTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllWalletDetailsByBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where balance equals to DEFAULT_BALANCE
        defaultWalletDetailsShouldBeFound("balance.equals=" + DEFAULT_BALANCE);

        // Get all the walletDetailsList where balance equals to UPDATED_BALANCE
        defaultWalletDetailsShouldNotBeFound("balance.equals=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllWalletDetailsByBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where balance in DEFAULT_BALANCE or UPDATED_BALANCE
        defaultWalletDetailsShouldBeFound("balance.in=" + DEFAULT_BALANCE + "," + UPDATED_BALANCE);

        // Get all the walletDetailsList where balance equals to UPDATED_BALANCE
        defaultWalletDetailsShouldNotBeFound("balance.in=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllWalletDetailsByBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where balance is not null
        defaultWalletDetailsShouldBeFound("balance.specified=true");

        // Get all the walletDetailsList where balance is null
        defaultWalletDetailsShouldNotBeFound("balance.specified=false");
    }

    @Test
    @Transactional
    public void getAllWalletDetailsByTicketIsEqualToSomething() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where ticket equals to DEFAULT_TICKET
        defaultWalletDetailsShouldBeFound("ticket.equals=" + DEFAULT_TICKET);

        // Get all the walletDetailsList where ticket equals to UPDATED_TICKET
        defaultWalletDetailsShouldNotBeFound("ticket.equals=" + UPDATED_TICKET);
    }

    @Test
    @Transactional
    public void getAllWalletDetailsByTicketIsInShouldWork() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where ticket in DEFAULT_TICKET or UPDATED_TICKET
        defaultWalletDetailsShouldBeFound("ticket.in=" + DEFAULT_TICKET + "," + UPDATED_TICKET);

        // Get all the walletDetailsList where ticket equals to UPDATED_TICKET
        defaultWalletDetailsShouldNotBeFound("ticket.in=" + UPDATED_TICKET);
    }

    @Test
    @Transactional
    public void getAllWalletDetailsByTicketIsNullOrNotNull() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where ticket is not null
        defaultWalletDetailsShouldBeFound("ticket.specified=true");

        // Get all the walletDetailsList where ticket is null
        defaultWalletDetailsShouldNotBeFound("ticket.specified=false");
    }

    @Test
    @Transactional
    public void getAllWalletDetailsByIntegralIsEqualToSomething() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where integral equals to DEFAULT_INTEGRAL
        defaultWalletDetailsShouldBeFound("integral.equals=" + DEFAULT_INTEGRAL);

        // Get all the walletDetailsList where integral equals to UPDATED_INTEGRAL
        defaultWalletDetailsShouldNotBeFound("integral.equals=" + UPDATED_INTEGRAL);
    }

    @Test
    @Transactional
    public void getAllWalletDetailsByIntegralIsInShouldWork() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where integral in DEFAULT_INTEGRAL or UPDATED_INTEGRAL
        defaultWalletDetailsShouldBeFound("integral.in=" + DEFAULT_INTEGRAL + "," + UPDATED_INTEGRAL);

        // Get all the walletDetailsList where integral equals to UPDATED_INTEGRAL
        defaultWalletDetailsShouldNotBeFound("integral.in=" + UPDATED_INTEGRAL);
    }

    @Test
    @Transactional
    public void getAllWalletDetailsByIntegralIsNullOrNotNull() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where integral is not null
        defaultWalletDetailsShouldBeFound("integral.specified=true");

        // Get all the walletDetailsList where integral is null
        defaultWalletDetailsShouldNotBeFound("integral.specified=false");
    }

    @Test
    @Transactional
    public void getAllWalletDetailsByPay_priceIsEqualToSomething() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where pay_price equals to DEFAULT_PAY_PRICE
        defaultWalletDetailsShouldBeFound("pay_price.equals=" + DEFAULT_PAY_PRICE);

        // Get all the walletDetailsList where pay_price equals to UPDATED_PAY_PRICE
        defaultWalletDetailsShouldNotBeFound("pay_price.equals=" + UPDATED_PAY_PRICE);
    }

    @Test
    @Transactional
    public void getAllWalletDetailsByPay_priceIsInShouldWork() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where pay_price in DEFAULT_PAY_PRICE or UPDATED_PAY_PRICE
        defaultWalletDetailsShouldBeFound("pay_price.in=" + DEFAULT_PAY_PRICE + "," + UPDATED_PAY_PRICE);

        // Get all the walletDetailsList where pay_price equals to UPDATED_PAY_PRICE
        defaultWalletDetailsShouldNotBeFound("pay_price.in=" + UPDATED_PAY_PRICE);
    }

    @Test
    @Transactional
    public void getAllWalletDetailsByPay_priceIsNullOrNotNull() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where pay_price is not null
        defaultWalletDetailsShouldBeFound("pay_price.specified=true");

        // Get all the walletDetailsList where pay_price is null
        defaultWalletDetailsShouldNotBeFound("pay_price.specified=false");
    }

    @Test
    @Transactional
    public void getAllWalletDetailsByOrderNoIsEqualToSomething() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where orderNo equals to DEFAULT_ORDER_NO
        defaultWalletDetailsShouldBeFound("orderNo.equals=" + DEFAULT_ORDER_NO);

        // Get all the walletDetailsList where orderNo equals to UPDATED_ORDER_NO
        defaultWalletDetailsShouldNotBeFound("orderNo.equals=" + UPDATED_ORDER_NO);
    }

    @Test
    @Transactional
    public void getAllWalletDetailsByOrderNoIsInShouldWork() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where orderNo in DEFAULT_ORDER_NO or UPDATED_ORDER_NO
        defaultWalletDetailsShouldBeFound("orderNo.in=" + DEFAULT_ORDER_NO + "," + UPDATED_ORDER_NO);

        // Get all the walletDetailsList where orderNo equals to UPDATED_ORDER_NO
        defaultWalletDetailsShouldNotBeFound("orderNo.in=" + UPDATED_ORDER_NO);
    }

    @Test
    @Transactional
    public void getAllWalletDetailsByOrderNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where orderNo is not null
        defaultWalletDetailsShouldBeFound("orderNo.specified=true");

        // Get all the walletDetailsList where orderNo is null
        defaultWalletDetailsShouldNotBeFound("orderNo.specified=false");
    }

    @Test
    @Transactional
    public void getAllWalletDetailsByWalletIsEqualToSomething() throws Exception {
        // Initialize the database
        Wallet wallet = WalletResourceIntTest.createEntity(em);
        em.persist(wallet);
        em.flush();
        walletDetails.setWallet(wallet);
        walletDetailsRepository.saveAndFlush(walletDetails);
        Long walletId = wallet.getId();

        // Get all the walletDetailsList where wallet equals to walletId
        defaultWalletDetailsShouldBeFound("walletId.equals=" + walletId);

        // Get all the walletDetailsList where wallet equals to walletId + 1
        defaultWalletDetailsShouldNotBeFound("walletId.equals=" + (walletId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultWalletDetailsShouldBeFound(String filter) throws Exception {
        restWalletDetailsMockMvc.perform(get("/api/wallet-details?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(walletDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].userid").value(hasItem(DEFAULT_USERID.intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(DEFAULT_CREATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.intValue())))
            .andExpect(jsonPath("$.[*].ticket").value(hasItem(DEFAULT_TICKET.intValue())))
            .andExpect(jsonPath("$.[*].integral").value(hasItem(DEFAULT_INTEGRAL.intValue())))
            .andExpect(jsonPath("$.[*].pay_price").value(hasItem(DEFAULT_PAY_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].orderNo").value(hasItem(DEFAULT_ORDER_NO.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultWalletDetailsShouldNotBeFound(String filter) throws Exception {
        restWalletDetailsMockMvc.perform(get("/api/wallet-details?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingWalletDetails() throws Exception {
        // Get the walletDetails
        restWalletDetailsMockMvc.perform(get("/api/wallet-details/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWalletDetails() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);
        int databaseSizeBeforeUpdate = walletDetailsRepository.findAll().size();

        // Update the walletDetails
        WalletDetails updatedWalletDetails = walletDetailsRepository.findOne(walletDetails.getId());
        // Disconnect from session so that the updates on updatedWalletDetails are not directly saved in db
        em.detach(updatedWalletDetails);
        updatedWalletDetails
            .userid(UPDATED_USERID)
            .amount(UPDATED_AMOUNT)
            .type(UPDATED_TYPE)
            .createdTime(UPDATED_CREATED_TIME)
            .balance(UPDATED_BALANCE)
            .ticket(UPDATED_TICKET)
            .integral(UPDATED_INTEGRAL)
            .pay_price(UPDATED_PAY_PRICE)
            .orderNo(UPDATED_ORDER_NO);
        WalletDetailsDTO walletDetailsDTO = walletDetailsMapper.toDto(updatedWalletDetails);

        restWalletDetailsMockMvc.perform(put("/api/wallet-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(walletDetailsDTO)))
            .andExpect(status().isOk());

        // Validate the WalletDetails in the database
        List<WalletDetails> walletDetailsList = walletDetailsRepository.findAll();
        assertThat(walletDetailsList).hasSize(databaseSizeBeforeUpdate);
        WalletDetails testWalletDetails = walletDetailsList.get(walletDetailsList.size() - 1);
        assertThat(testWalletDetails.getUserid()).isEqualTo(UPDATED_USERID);
        assertThat(testWalletDetails.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testWalletDetails.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testWalletDetails.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testWalletDetails.getBalance()).isEqualTo(UPDATED_BALANCE);
        assertThat(testWalletDetails.getTicket()).isEqualTo(UPDATED_TICKET);
        assertThat(testWalletDetails.getIntegral()).isEqualTo(UPDATED_INTEGRAL);
        assertThat(testWalletDetails.getPay_price()).isEqualTo(UPDATED_PAY_PRICE);
        assertThat(testWalletDetails.getOrderNo()).isEqualTo(UPDATED_ORDER_NO);
    }

    @Test
    @Transactional
    public void updateNonExistingWalletDetails() throws Exception {
        int databaseSizeBeforeUpdate = walletDetailsRepository.findAll().size();

        // Create the WalletDetails
        WalletDetailsDTO walletDetailsDTO = walletDetailsMapper.toDto(walletDetails);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWalletDetailsMockMvc.perform(put("/api/wallet-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(walletDetailsDTO)))
            .andExpect(status().isCreated());

        // Validate the WalletDetails in the database
        List<WalletDetails> walletDetailsList = walletDetailsRepository.findAll();
        assertThat(walletDetailsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteWalletDetails() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);
        int databaseSizeBeforeDelete = walletDetailsRepository.findAll().size();

        // Get the walletDetails
        restWalletDetailsMockMvc.perform(delete("/api/wallet-details/{id}", walletDetails.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<WalletDetails> walletDetailsList = walletDetailsRepository.findAll();
        assertThat(walletDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WalletDetails.class);
        WalletDetails walletDetails1 = new WalletDetails();
        walletDetails1.setId(1L);
        WalletDetails walletDetails2 = new WalletDetails();
        walletDetails2.setId(walletDetails1.getId());
        assertThat(walletDetails1).isEqualTo(walletDetails2);
        walletDetails2.setId(2L);
        assertThat(walletDetails1).isNotEqualTo(walletDetails2);
        walletDetails1.setId(null);
        assertThat(walletDetails1).isNotEqualTo(walletDetails2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WalletDetailsDTO.class);
        WalletDetailsDTO walletDetailsDTO1 = new WalletDetailsDTO();
        walletDetailsDTO1.setId(1L);
        WalletDetailsDTO walletDetailsDTO2 = new WalletDetailsDTO();
        assertThat(walletDetailsDTO1).isNotEqualTo(walletDetailsDTO2);
        walletDetailsDTO2.setId(walletDetailsDTO1.getId());
        assertThat(walletDetailsDTO1).isEqualTo(walletDetailsDTO2);
        walletDetailsDTO2.setId(2L);
        assertThat(walletDetailsDTO1).isNotEqualTo(walletDetailsDTO2);
        walletDetailsDTO1.setId(null);
        assertThat(walletDetailsDTO1).isNotEqualTo(walletDetailsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(walletDetailsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(walletDetailsMapper.fromId(null)).isNull();
    }
}
