package com.eyun.wallet.web.rest;

import com.eyun.wallet.WalletApp;

import com.eyun.wallet.config.SecurityBeanOverrideConfiguration;

import com.eyun.wallet.domain.WalletDetails;
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

    private static final Long DEFAULT_WALLET_ID = 1L;
    private static final Long UPDATED_WALLET_ID = 2L;

    private static final Long DEFAULT_AMOUNT = 1L;
    private static final Long UPDATED_AMOUNT = 2L;

    private static final Integer DEFAULT_TYPE = 1;
    private static final Integer UPDATED_TYPE = 2;

    private static final Long DEFAULT_BALANCE = 1L;
    private static final Long UPDATED_BALANCE = 2L;

    private static final Long DEFAULT_TICKET = 1L;
    private static final Long UPDATED_TICKET = 2L;

    private static final Long DEFAULT_INTEGRAL = 1L;
    private static final Long UPDATED_INTEGRAL = 2L;

    private static final Long DEFAULT_PAY_PRICE = 1L;
    private static final Long UPDATED_PAY_PRICE = 2L;

    private static final Long DEFAULT_ORDER_ID = 1L;
    private static final Long UPDATED_ORDER_ID = 2L;

    private static final Instant DEFAULT_CREATED_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

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
            .walletId(DEFAULT_WALLET_ID)
            .amount(DEFAULT_AMOUNT)
            .type(DEFAULT_TYPE)
            .balance(DEFAULT_BALANCE)
            .ticket(DEFAULT_TICKET)
            .integral(DEFAULT_INTEGRAL)
            .payPrice(DEFAULT_PAY_PRICE)
            .orderId(DEFAULT_ORDER_ID)
            .createdTime(DEFAULT_CREATED_TIME);
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
        assertThat(testWalletDetails.getWalletId()).isEqualTo(DEFAULT_WALLET_ID);
        assertThat(testWalletDetails.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testWalletDetails.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testWalletDetails.getBalance()).isEqualTo(DEFAULT_BALANCE);
        assertThat(testWalletDetails.getTicket()).isEqualTo(DEFAULT_TICKET);
        assertThat(testWalletDetails.getIntegral()).isEqualTo(DEFAULT_INTEGRAL);
        assertThat(testWalletDetails.getPayPrice()).isEqualTo(DEFAULT_PAY_PRICE);
        assertThat(testWalletDetails.getOrderId()).isEqualTo(DEFAULT_ORDER_ID);
        assertThat(testWalletDetails.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
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
    public void checkWalletIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = walletDetailsRepository.findAll().size();
        // set the field null
        walletDetails.setWalletId(null);

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
            .andExpect(jsonPath("$.[*].walletId").value(hasItem(DEFAULT_WALLET_ID.intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.intValue())))
            .andExpect(jsonPath("$.[*].ticket").value(hasItem(DEFAULT_TICKET.intValue())))
            .andExpect(jsonPath("$.[*].integral").value(hasItem(DEFAULT_INTEGRAL.intValue())))
            .andExpect(jsonPath("$.[*].payPrice").value(hasItem(DEFAULT_PAY_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].orderId").value(hasItem(DEFAULT_ORDER_ID.intValue())))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(DEFAULT_CREATED_TIME.toString())));
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
            .andExpect(jsonPath("$.walletId").value(DEFAULT_WALLET_ID.intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.intValue()))
            .andExpect(jsonPath("$.ticket").value(DEFAULT_TICKET.intValue()))
            .andExpect(jsonPath("$.integral").value(DEFAULT_INTEGRAL.intValue()))
            .andExpect(jsonPath("$.payPrice").value(DEFAULT_PAY_PRICE.intValue()))
            .andExpect(jsonPath("$.orderId").value(DEFAULT_ORDER_ID.intValue()))
            .andExpect(jsonPath("$.createdTime").value(DEFAULT_CREATED_TIME.toString()));
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
    public void getAllWalletDetailsByWalletIdIsEqualToSomething() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where walletId equals to DEFAULT_WALLET_ID
        defaultWalletDetailsShouldBeFound("walletId.equals=" + DEFAULT_WALLET_ID);

        // Get all the walletDetailsList where walletId equals to UPDATED_WALLET_ID
        defaultWalletDetailsShouldNotBeFound("walletId.equals=" + UPDATED_WALLET_ID);
    }

    @Test
    @Transactional
    public void getAllWalletDetailsByWalletIdIsInShouldWork() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where walletId in DEFAULT_WALLET_ID or UPDATED_WALLET_ID
        defaultWalletDetailsShouldBeFound("walletId.in=" + DEFAULT_WALLET_ID + "," + UPDATED_WALLET_ID);

        // Get all the walletDetailsList where walletId equals to UPDATED_WALLET_ID
        defaultWalletDetailsShouldNotBeFound("walletId.in=" + UPDATED_WALLET_ID);
    }

    @Test
    @Transactional
    public void getAllWalletDetailsByWalletIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where walletId is not null
        defaultWalletDetailsShouldBeFound("walletId.specified=true");

        // Get all the walletDetailsList where walletId is null
        defaultWalletDetailsShouldNotBeFound("walletId.specified=false");
    }

    @Test
    @Transactional
    public void getAllWalletDetailsByWalletIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where walletId greater than or equals to DEFAULT_WALLET_ID
        defaultWalletDetailsShouldBeFound("walletId.greaterOrEqualThan=" + DEFAULT_WALLET_ID);

        // Get all the walletDetailsList where walletId greater than or equals to UPDATED_WALLET_ID
        defaultWalletDetailsShouldNotBeFound("walletId.greaterOrEqualThan=" + UPDATED_WALLET_ID);
    }

    @Test
    @Transactional
    public void getAllWalletDetailsByWalletIdIsLessThanSomething() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where walletId less than or equals to DEFAULT_WALLET_ID
        defaultWalletDetailsShouldNotBeFound("walletId.lessThan=" + DEFAULT_WALLET_ID);

        // Get all the walletDetailsList where walletId less than or equals to UPDATED_WALLET_ID
        defaultWalletDetailsShouldBeFound("walletId.lessThan=" + UPDATED_WALLET_ID);
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
    public void getAllWalletDetailsByBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where balance greater than or equals to DEFAULT_BALANCE
        defaultWalletDetailsShouldBeFound("balance.greaterOrEqualThan=" + DEFAULT_BALANCE);

        // Get all the walletDetailsList where balance greater than or equals to UPDATED_BALANCE
        defaultWalletDetailsShouldNotBeFound("balance.greaterOrEqualThan=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllWalletDetailsByBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where balance less than or equals to DEFAULT_BALANCE
        defaultWalletDetailsShouldNotBeFound("balance.lessThan=" + DEFAULT_BALANCE);

        // Get all the walletDetailsList where balance less than or equals to UPDATED_BALANCE
        defaultWalletDetailsShouldBeFound("balance.lessThan=" + UPDATED_BALANCE);
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
    public void getAllWalletDetailsByTicketIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where ticket greater than or equals to DEFAULT_TICKET
        defaultWalletDetailsShouldBeFound("ticket.greaterOrEqualThan=" + DEFAULT_TICKET);

        // Get all the walletDetailsList where ticket greater than or equals to UPDATED_TICKET
        defaultWalletDetailsShouldNotBeFound("ticket.greaterOrEqualThan=" + UPDATED_TICKET);
    }

    @Test
    @Transactional
    public void getAllWalletDetailsByTicketIsLessThanSomething() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where ticket less than or equals to DEFAULT_TICKET
        defaultWalletDetailsShouldNotBeFound("ticket.lessThan=" + DEFAULT_TICKET);

        // Get all the walletDetailsList where ticket less than or equals to UPDATED_TICKET
        defaultWalletDetailsShouldBeFound("ticket.lessThan=" + UPDATED_TICKET);
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
    public void getAllWalletDetailsByIntegralIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where integral greater than or equals to DEFAULT_INTEGRAL
        defaultWalletDetailsShouldBeFound("integral.greaterOrEqualThan=" + DEFAULT_INTEGRAL);

        // Get all the walletDetailsList where integral greater than or equals to UPDATED_INTEGRAL
        defaultWalletDetailsShouldNotBeFound("integral.greaterOrEqualThan=" + UPDATED_INTEGRAL);
    }

    @Test
    @Transactional
    public void getAllWalletDetailsByIntegralIsLessThanSomething() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where integral less than or equals to DEFAULT_INTEGRAL
        defaultWalletDetailsShouldNotBeFound("integral.lessThan=" + DEFAULT_INTEGRAL);

        // Get all the walletDetailsList where integral less than or equals to UPDATED_INTEGRAL
        defaultWalletDetailsShouldBeFound("integral.lessThan=" + UPDATED_INTEGRAL);
    }


    @Test
    @Transactional
    public void getAllWalletDetailsByPayPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where payPrice equals to DEFAULT_PAY_PRICE
        defaultWalletDetailsShouldBeFound("payPrice.equals=" + DEFAULT_PAY_PRICE);

        // Get all the walletDetailsList where payPrice equals to UPDATED_PAY_PRICE
        defaultWalletDetailsShouldNotBeFound("payPrice.equals=" + UPDATED_PAY_PRICE);
    }

    @Test
    @Transactional
    public void getAllWalletDetailsByPayPriceIsInShouldWork() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where payPrice in DEFAULT_PAY_PRICE or UPDATED_PAY_PRICE
        defaultWalletDetailsShouldBeFound("payPrice.in=" + DEFAULT_PAY_PRICE + "," + UPDATED_PAY_PRICE);

        // Get all the walletDetailsList where payPrice equals to UPDATED_PAY_PRICE
        defaultWalletDetailsShouldNotBeFound("payPrice.in=" + UPDATED_PAY_PRICE);
    }

    @Test
    @Transactional
    public void getAllWalletDetailsByPayPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where payPrice is not null
        defaultWalletDetailsShouldBeFound("payPrice.specified=true");

        // Get all the walletDetailsList where payPrice is null
        defaultWalletDetailsShouldNotBeFound("payPrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllWalletDetailsByPayPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where payPrice greater than or equals to DEFAULT_PAY_PRICE
        defaultWalletDetailsShouldBeFound("payPrice.greaterOrEqualThan=" + DEFAULT_PAY_PRICE);

        // Get all the walletDetailsList where payPrice greater than or equals to UPDATED_PAY_PRICE
        defaultWalletDetailsShouldNotBeFound("payPrice.greaterOrEqualThan=" + UPDATED_PAY_PRICE);
    }

    @Test
    @Transactional
    public void getAllWalletDetailsByPayPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where payPrice less than or equals to DEFAULT_PAY_PRICE
        defaultWalletDetailsShouldNotBeFound("payPrice.lessThan=" + DEFAULT_PAY_PRICE);

        // Get all the walletDetailsList where payPrice less than or equals to UPDATED_PAY_PRICE
        defaultWalletDetailsShouldBeFound("payPrice.lessThan=" + UPDATED_PAY_PRICE);
    }


    @Test
    @Transactional
    public void getAllWalletDetailsByOrderIdIsEqualToSomething() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where orderId equals to DEFAULT_ORDER_ID
        defaultWalletDetailsShouldBeFound("orderId.equals=" + DEFAULT_ORDER_ID);

        // Get all the walletDetailsList where orderId equals to UPDATED_ORDER_ID
        defaultWalletDetailsShouldNotBeFound("orderId.equals=" + UPDATED_ORDER_ID);
    }

    @Test
    @Transactional
    public void getAllWalletDetailsByOrderIdIsInShouldWork() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where orderId in DEFAULT_ORDER_ID or UPDATED_ORDER_ID
        defaultWalletDetailsShouldBeFound("orderId.in=" + DEFAULT_ORDER_ID + "," + UPDATED_ORDER_ID);

        // Get all the walletDetailsList where orderId equals to UPDATED_ORDER_ID
        defaultWalletDetailsShouldNotBeFound("orderId.in=" + UPDATED_ORDER_ID);
    }

    @Test
    @Transactional
    public void getAllWalletDetailsByOrderIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where orderId is not null
        defaultWalletDetailsShouldBeFound("orderId.specified=true");

        // Get all the walletDetailsList where orderId is null
        defaultWalletDetailsShouldNotBeFound("orderId.specified=false");
    }

    @Test
    @Transactional
    public void getAllWalletDetailsByOrderIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where orderId greater than or equals to DEFAULT_ORDER_ID
        defaultWalletDetailsShouldBeFound("orderId.greaterOrEqualThan=" + DEFAULT_ORDER_ID);

        // Get all the walletDetailsList where orderId greater than or equals to UPDATED_ORDER_ID
        defaultWalletDetailsShouldNotBeFound("orderId.greaterOrEqualThan=" + UPDATED_ORDER_ID);
    }

    @Test
    @Transactional
    public void getAllWalletDetailsByOrderIdIsLessThanSomething() throws Exception {
        // Initialize the database
        walletDetailsRepository.saveAndFlush(walletDetails);

        // Get all the walletDetailsList where orderId less than or equals to DEFAULT_ORDER_ID
        defaultWalletDetailsShouldNotBeFound("orderId.lessThan=" + DEFAULT_ORDER_ID);

        // Get all the walletDetailsList where orderId less than or equals to UPDATED_ORDER_ID
        defaultWalletDetailsShouldBeFound("orderId.lessThan=" + UPDATED_ORDER_ID);
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
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultWalletDetailsShouldBeFound(String filter) throws Exception {
        restWalletDetailsMockMvc.perform(get("/api/wallet-details?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(walletDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].userid").value(hasItem(DEFAULT_USERID.intValue())))
            .andExpect(jsonPath("$.[*].walletId").value(hasItem(DEFAULT_WALLET_ID.intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.intValue())))
            .andExpect(jsonPath("$.[*].ticket").value(hasItem(DEFAULT_TICKET.intValue())))
            .andExpect(jsonPath("$.[*].integral").value(hasItem(DEFAULT_INTEGRAL.intValue())))
            .andExpect(jsonPath("$.[*].payPrice").value(hasItem(DEFAULT_PAY_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].orderId").value(hasItem(DEFAULT_ORDER_ID.intValue())))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(DEFAULT_CREATED_TIME.toString())));
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
            .walletId(UPDATED_WALLET_ID)
            .amount(UPDATED_AMOUNT)
            .type(UPDATED_TYPE)
            .balance(UPDATED_BALANCE)
            .ticket(UPDATED_TICKET)
            .integral(UPDATED_INTEGRAL)
            .payPrice(UPDATED_PAY_PRICE)
            .orderId(UPDATED_ORDER_ID)
            .createdTime(UPDATED_CREATED_TIME);
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
        assertThat(testWalletDetails.getWalletId()).isEqualTo(UPDATED_WALLET_ID);
        assertThat(testWalletDetails.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testWalletDetails.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testWalletDetails.getBalance()).isEqualTo(UPDATED_BALANCE);
        assertThat(testWalletDetails.getTicket()).isEqualTo(UPDATED_TICKET);
        assertThat(testWalletDetails.getIntegral()).isEqualTo(UPDATED_INTEGRAL);
        assertThat(testWalletDetails.getPayPrice()).isEqualTo(UPDATED_PAY_PRICE);
        assertThat(testWalletDetails.getOrderId()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testWalletDetails.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
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
