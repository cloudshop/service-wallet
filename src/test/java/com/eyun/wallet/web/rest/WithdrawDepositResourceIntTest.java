package com.eyun.wallet.web.rest;

import com.eyun.wallet.WalletApp;

import com.eyun.wallet.config.SecurityBeanOverrideConfiguration;

import com.eyun.wallet.domain.WithdrawDeposit;
import com.eyun.wallet.domain.Wallet;
import com.eyun.wallet.repository.WithdrawDepositRepository;
import com.eyun.wallet.service.WithdrawDepositService;
import com.eyun.wallet.service.dto.WithdrawDepositDTO;
import com.eyun.wallet.service.mapper.WithdrawDepositMapper;
import com.eyun.wallet.web.rest.errors.ExceptionTranslator;
import com.eyun.wallet.service.dto.WithdrawDepositCriteria;
import com.eyun.wallet.service.WithdrawDepositQueryService;

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
 * Test class for the WithdrawDepositResource REST controller.
 *
 * @see WithdrawDepositResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WalletApp.class, SecurityBeanOverrideConfiguration.class})
public class WithdrawDepositResourceIntTest {

    private static final String DEFAULT_CARDHOLDER = "AAAAAAAAAA";
    private static final String UPDATED_CARDHOLDER = "BBBBBBBBBB";

    private static final String DEFAULT_OPENING_BANK = "AAAAAAAAAA";
    private static final String UPDATED_OPENING_BANK = "BBBBBBBBBB";

    private static final String DEFAULT_BANKCARD_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_BANKCARD_NUMBER = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_MONEY = new BigDecimal(1);
    private static final BigDecimal UPDATED_MONEY = new BigDecimal(2);

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    private static final String DEFAULT_STATUS_STRING = "AAAAAAAAAA";
    private static final String UPDATED_STATUS_STRING = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_USERID = 1L;
    private static final Long UPDATED_USERID = 2L;

    @Autowired
    private WithdrawDepositRepository withdrawDepositRepository;

    @Autowired
    private WithdrawDepositMapper withdrawDepositMapper;

    @Autowired
    private WithdrawDepositService withdrawDepositService;

    @Autowired
    private WithdrawDepositQueryService withdrawDepositQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWithdrawDepositMockMvc;

    private WithdrawDeposit withdrawDeposit;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WithdrawDepositResource withdrawDepositResource = new WithdrawDepositResource(withdrawDepositService, withdrawDepositQueryService);
        this.restWithdrawDepositMockMvc = MockMvcBuilders.standaloneSetup(withdrawDepositResource)
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
    public static WithdrawDeposit createEntity(EntityManager em) {
        WithdrawDeposit withdrawDeposit = new WithdrawDeposit()
            .cardholder(DEFAULT_CARDHOLDER)
            .openingBank(DEFAULT_OPENING_BANK)
            .bankcardNumber(DEFAULT_BANKCARD_NUMBER)
            .money(DEFAULT_MONEY)
            .status(DEFAULT_STATUS)
            .statusString(DEFAULT_STATUS_STRING)
            .createdTime(DEFAULT_CREATED_TIME)
            .updatedTime(DEFAULT_UPDATED_TIME)
            .userid(DEFAULT_USERID);
        return withdrawDeposit;
    }

    @Before
    public void initTest() {
        withdrawDeposit = createEntity(em);
    }

    @Test
    @Transactional
    public void createWithdrawDeposit() throws Exception {
        int databaseSizeBeforeCreate = withdrawDepositRepository.findAll().size();

        // Create the WithdrawDeposit
        WithdrawDepositDTO withdrawDepositDTO = withdrawDepositMapper.toDto(withdrawDeposit);
        restWithdrawDepositMockMvc.perform(post("/api/withdraw-deposits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(withdrawDepositDTO)))
            .andExpect(status().isCreated());

        // Validate the WithdrawDeposit in the database
        List<WithdrawDeposit> withdrawDepositList = withdrawDepositRepository.findAll();
        assertThat(withdrawDepositList).hasSize(databaseSizeBeforeCreate + 1);
        WithdrawDeposit testWithdrawDeposit = withdrawDepositList.get(withdrawDepositList.size() - 1);
        assertThat(testWithdrawDeposit.getCardholder()).isEqualTo(DEFAULT_CARDHOLDER);
        assertThat(testWithdrawDeposit.getOpeningBank()).isEqualTo(DEFAULT_OPENING_BANK);
        assertThat(testWithdrawDeposit.getBankcardNumber()).isEqualTo(DEFAULT_BANKCARD_NUMBER);
        assertThat(testWithdrawDeposit.getMoney()).isEqualTo(DEFAULT_MONEY);
        assertThat(testWithdrawDeposit.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testWithdrawDeposit.getStatusString()).isEqualTo(DEFAULT_STATUS_STRING);
        assertThat(testWithdrawDeposit.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testWithdrawDeposit.getUpdatedTime()).isEqualTo(DEFAULT_UPDATED_TIME);
        assertThat(testWithdrawDeposit.getUserid()).isEqualTo(DEFAULT_USERID);
    }

    @Test
    @Transactional
    public void createWithdrawDepositWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = withdrawDepositRepository.findAll().size();

        // Create the WithdrawDeposit with an existing ID
        withdrawDeposit.setId(1L);
        WithdrawDepositDTO withdrawDepositDTO = withdrawDepositMapper.toDto(withdrawDeposit);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWithdrawDepositMockMvc.perform(post("/api/withdraw-deposits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(withdrawDepositDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WithdrawDeposit in the database
        List<WithdrawDeposit> withdrawDepositList = withdrawDepositRepository.findAll();
        assertThat(withdrawDepositList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllWithdrawDeposits() throws Exception {
        // Initialize the database
        withdrawDepositRepository.saveAndFlush(withdrawDeposit);

        // Get all the withdrawDepositList
        restWithdrawDepositMockMvc.perform(get("/api/withdraw-deposits?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(withdrawDeposit.getId().intValue())))
            .andExpect(jsonPath("$.[*].cardholder").value(hasItem(DEFAULT_CARDHOLDER.toString())))
            .andExpect(jsonPath("$.[*].openingBank").value(hasItem(DEFAULT_OPENING_BANK.toString())))
            .andExpect(jsonPath("$.[*].bankcardNumber").value(hasItem(DEFAULT_BANKCARD_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].money").value(hasItem(DEFAULT_MONEY.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].statusString").value(hasItem(DEFAULT_STATUS_STRING.toString())))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(DEFAULT_CREATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].updatedTime").value(hasItem(DEFAULT_UPDATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].userid").value(hasItem(DEFAULT_USERID.intValue())));
    }

    @Test
    @Transactional
    public void getWithdrawDeposit() throws Exception {
        // Initialize the database
        withdrawDepositRepository.saveAndFlush(withdrawDeposit);

        // Get the withdrawDeposit
        restWithdrawDepositMockMvc.perform(get("/api/withdraw-deposits/{id}", withdrawDeposit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(withdrawDeposit.getId().intValue()))
            .andExpect(jsonPath("$.cardholder").value(DEFAULT_CARDHOLDER.toString()))
            .andExpect(jsonPath("$.openingBank").value(DEFAULT_OPENING_BANK.toString()))
            .andExpect(jsonPath("$.bankcardNumber").value(DEFAULT_BANKCARD_NUMBER.toString()))
            .andExpect(jsonPath("$.money").value(DEFAULT_MONEY.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.statusString").value(DEFAULT_STATUS_STRING.toString()))
            .andExpect(jsonPath("$.createdTime").value(DEFAULT_CREATED_TIME.toString()))
            .andExpect(jsonPath("$.updatedTime").value(DEFAULT_UPDATED_TIME.toString()))
            .andExpect(jsonPath("$.userid").value(DEFAULT_USERID.intValue()));
    }

    @Test
    @Transactional
    public void getAllWithdrawDepositsByCardholderIsEqualToSomething() throws Exception {
        // Initialize the database
        withdrawDepositRepository.saveAndFlush(withdrawDeposit);

        // Get all the withdrawDepositList where cardholder equals to DEFAULT_CARDHOLDER
        defaultWithdrawDepositShouldBeFound("cardholder.equals=" + DEFAULT_CARDHOLDER);

        // Get all the withdrawDepositList where cardholder equals to UPDATED_CARDHOLDER
        defaultWithdrawDepositShouldNotBeFound("cardholder.equals=" + UPDATED_CARDHOLDER);
    }

    @Test
    @Transactional
    public void getAllWithdrawDepositsByCardholderIsInShouldWork() throws Exception {
        // Initialize the database
        withdrawDepositRepository.saveAndFlush(withdrawDeposit);

        // Get all the withdrawDepositList where cardholder in DEFAULT_CARDHOLDER or UPDATED_CARDHOLDER
        defaultWithdrawDepositShouldBeFound("cardholder.in=" + DEFAULT_CARDHOLDER + "," + UPDATED_CARDHOLDER);

        // Get all the withdrawDepositList where cardholder equals to UPDATED_CARDHOLDER
        defaultWithdrawDepositShouldNotBeFound("cardholder.in=" + UPDATED_CARDHOLDER);
    }

    @Test
    @Transactional
    public void getAllWithdrawDepositsByCardholderIsNullOrNotNull() throws Exception {
        // Initialize the database
        withdrawDepositRepository.saveAndFlush(withdrawDeposit);

        // Get all the withdrawDepositList where cardholder is not null
        defaultWithdrawDepositShouldBeFound("cardholder.specified=true");

        // Get all the withdrawDepositList where cardholder is null
        defaultWithdrawDepositShouldNotBeFound("cardholder.specified=false");
    }

    @Test
    @Transactional
    public void getAllWithdrawDepositsByOpeningBankIsEqualToSomething() throws Exception {
        // Initialize the database
        withdrawDepositRepository.saveAndFlush(withdrawDeposit);

        // Get all the withdrawDepositList where openingBank equals to DEFAULT_OPENING_BANK
        defaultWithdrawDepositShouldBeFound("openingBank.equals=" + DEFAULT_OPENING_BANK);

        // Get all the withdrawDepositList where openingBank equals to UPDATED_OPENING_BANK
        defaultWithdrawDepositShouldNotBeFound("openingBank.equals=" + UPDATED_OPENING_BANK);
    }

    @Test
    @Transactional
    public void getAllWithdrawDepositsByOpeningBankIsInShouldWork() throws Exception {
        // Initialize the database
        withdrawDepositRepository.saveAndFlush(withdrawDeposit);

        // Get all the withdrawDepositList where openingBank in DEFAULT_OPENING_BANK or UPDATED_OPENING_BANK
        defaultWithdrawDepositShouldBeFound("openingBank.in=" + DEFAULT_OPENING_BANK + "," + UPDATED_OPENING_BANK);

        // Get all the withdrawDepositList where openingBank equals to UPDATED_OPENING_BANK
        defaultWithdrawDepositShouldNotBeFound("openingBank.in=" + UPDATED_OPENING_BANK);
    }

    @Test
    @Transactional
    public void getAllWithdrawDepositsByOpeningBankIsNullOrNotNull() throws Exception {
        // Initialize the database
        withdrawDepositRepository.saveAndFlush(withdrawDeposit);

        // Get all the withdrawDepositList where openingBank is not null
        defaultWithdrawDepositShouldBeFound("openingBank.specified=true");

        // Get all the withdrawDepositList where openingBank is null
        defaultWithdrawDepositShouldNotBeFound("openingBank.specified=false");
    }

    @Test
    @Transactional
    public void getAllWithdrawDepositsByBankcardNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        withdrawDepositRepository.saveAndFlush(withdrawDeposit);

        // Get all the withdrawDepositList where bankcardNumber equals to DEFAULT_BANKCARD_NUMBER
        defaultWithdrawDepositShouldBeFound("bankcardNumber.equals=" + DEFAULT_BANKCARD_NUMBER);

        // Get all the withdrawDepositList where bankcardNumber equals to UPDATED_BANKCARD_NUMBER
        defaultWithdrawDepositShouldNotBeFound("bankcardNumber.equals=" + UPDATED_BANKCARD_NUMBER);
    }

    @Test
    @Transactional
    public void getAllWithdrawDepositsByBankcardNumberIsInShouldWork() throws Exception {
        // Initialize the database
        withdrawDepositRepository.saveAndFlush(withdrawDeposit);

        // Get all the withdrawDepositList where bankcardNumber in DEFAULT_BANKCARD_NUMBER or UPDATED_BANKCARD_NUMBER
        defaultWithdrawDepositShouldBeFound("bankcardNumber.in=" + DEFAULT_BANKCARD_NUMBER + "," + UPDATED_BANKCARD_NUMBER);

        // Get all the withdrawDepositList where bankcardNumber equals to UPDATED_BANKCARD_NUMBER
        defaultWithdrawDepositShouldNotBeFound("bankcardNumber.in=" + UPDATED_BANKCARD_NUMBER);
    }

    @Test
    @Transactional
    public void getAllWithdrawDepositsByBankcardNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        withdrawDepositRepository.saveAndFlush(withdrawDeposit);

        // Get all the withdrawDepositList where bankcardNumber is not null
        defaultWithdrawDepositShouldBeFound("bankcardNumber.specified=true");

        // Get all the withdrawDepositList where bankcardNumber is null
        defaultWithdrawDepositShouldNotBeFound("bankcardNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllWithdrawDepositsByMoneyIsEqualToSomething() throws Exception {
        // Initialize the database
        withdrawDepositRepository.saveAndFlush(withdrawDeposit);

        // Get all the withdrawDepositList where money equals to DEFAULT_MONEY
        defaultWithdrawDepositShouldBeFound("money.equals=" + DEFAULT_MONEY);

        // Get all the withdrawDepositList where money equals to UPDATED_MONEY
        defaultWithdrawDepositShouldNotBeFound("money.equals=" + UPDATED_MONEY);
    }

    @Test
    @Transactional
    public void getAllWithdrawDepositsByMoneyIsInShouldWork() throws Exception {
        // Initialize the database
        withdrawDepositRepository.saveAndFlush(withdrawDeposit);

        // Get all the withdrawDepositList where money in DEFAULT_MONEY or UPDATED_MONEY
        defaultWithdrawDepositShouldBeFound("money.in=" + DEFAULT_MONEY + "," + UPDATED_MONEY);

        // Get all the withdrawDepositList where money equals to UPDATED_MONEY
        defaultWithdrawDepositShouldNotBeFound("money.in=" + UPDATED_MONEY);
    }

    @Test
    @Transactional
    public void getAllWithdrawDepositsByMoneyIsNullOrNotNull() throws Exception {
        // Initialize the database
        withdrawDepositRepository.saveAndFlush(withdrawDeposit);

        // Get all the withdrawDepositList where money is not null
        defaultWithdrawDepositShouldBeFound("money.specified=true");

        // Get all the withdrawDepositList where money is null
        defaultWithdrawDepositShouldNotBeFound("money.specified=false");
    }

    @Test
    @Transactional
    public void getAllWithdrawDepositsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        withdrawDepositRepository.saveAndFlush(withdrawDeposit);

        // Get all the withdrawDepositList where status equals to DEFAULT_STATUS
        defaultWithdrawDepositShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the withdrawDepositList where status equals to UPDATED_STATUS
        defaultWithdrawDepositShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllWithdrawDepositsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        withdrawDepositRepository.saveAndFlush(withdrawDeposit);

        // Get all the withdrawDepositList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultWithdrawDepositShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the withdrawDepositList where status equals to UPDATED_STATUS
        defaultWithdrawDepositShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllWithdrawDepositsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        withdrawDepositRepository.saveAndFlush(withdrawDeposit);

        // Get all the withdrawDepositList where status is not null
        defaultWithdrawDepositShouldBeFound("status.specified=true");

        // Get all the withdrawDepositList where status is null
        defaultWithdrawDepositShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllWithdrawDepositsByStatusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        withdrawDepositRepository.saveAndFlush(withdrawDeposit);

        // Get all the withdrawDepositList where status greater than or equals to DEFAULT_STATUS
        defaultWithdrawDepositShouldBeFound("status.greaterOrEqualThan=" + DEFAULT_STATUS);

        // Get all the withdrawDepositList where status greater than or equals to UPDATED_STATUS
        defaultWithdrawDepositShouldNotBeFound("status.greaterOrEqualThan=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllWithdrawDepositsByStatusIsLessThanSomething() throws Exception {
        // Initialize the database
        withdrawDepositRepository.saveAndFlush(withdrawDeposit);

        // Get all the withdrawDepositList where status less than or equals to DEFAULT_STATUS
        defaultWithdrawDepositShouldNotBeFound("status.lessThan=" + DEFAULT_STATUS);

        // Get all the withdrawDepositList where status less than or equals to UPDATED_STATUS
        defaultWithdrawDepositShouldBeFound("status.lessThan=" + UPDATED_STATUS);
    }


    @Test
    @Transactional
    public void getAllWithdrawDepositsByStatusStringIsEqualToSomething() throws Exception {
        // Initialize the database
        withdrawDepositRepository.saveAndFlush(withdrawDeposit);

        // Get all the withdrawDepositList where statusString equals to DEFAULT_STATUS_STRING
        defaultWithdrawDepositShouldBeFound("statusString.equals=" + DEFAULT_STATUS_STRING);

        // Get all the withdrawDepositList where statusString equals to UPDATED_STATUS_STRING
        defaultWithdrawDepositShouldNotBeFound("statusString.equals=" + UPDATED_STATUS_STRING);
    }

    @Test
    @Transactional
    public void getAllWithdrawDepositsByStatusStringIsInShouldWork() throws Exception {
        // Initialize the database
        withdrawDepositRepository.saveAndFlush(withdrawDeposit);

        // Get all the withdrawDepositList where statusString in DEFAULT_STATUS_STRING or UPDATED_STATUS_STRING
        defaultWithdrawDepositShouldBeFound("statusString.in=" + DEFAULT_STATUS_STRING + "," + UPDATED_STATUS_STRING);

        // Get all the withdrawDepositList where statusString equals to UPDATED_STATUS_STRING
        defaultWithdrawDepositShouldNotBeFound("statusString.in=" + UPDATED_STATUS_STRING);
    }

    @Test
    @Transactional
    public void getAllWithdrawDepositsByStatusStringIsNullOrNotNull() throws Exception {
        // Initialize the database
        withdrawDepositRepository.saveAndFlush(withdrawDeposit);

        // Get all the withdrawDepositList where statusString is not null
        defaultWithdrawDepositShouldBeFound("statusString.specified=true");

        // Get all the withdrawDepositList where statusString is null
        defaultWithdrawDepositShouldNotBeFound("statusString.specified=false");
    }

    @Test
    @Transactional
    public void getAllWithdrawDepositsByCreatedTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        withdrawDepositRepository.saveAndFlush(withdrawDeposit);

        // Get all the withdrawDepositList where createdTime equals to DEFAULT_CREATED_TIME
        defaultWithdrawDepositShouldBeFound("createdTime.equals=" + DEFAULT_CREATED_TIME);

        // Get all the withdrawDepositList where createdTime equals to UPDATED_CREATED_TIME
        defaultWithdrawDepositShouldNotBeFound("createdTime.equals=" + UPDATED_CREATED_TIME);
    }

    @Test
    @Transactional
    public void getAllWithdrawDepositsByCreatedTimeIsInShouldWork() throws Exception {
        // Initialize the database
        withdrawDepositRepository.saveAndFlush(withdrawDeposit);

        // Get all the withdrawDepositList where createdTime in DEFAULT_CREATED_TIME or UPDATED_CREATED_TIME
        defaultWithdrawDepositShouldBeFound("createdTime.in=" + DEFAULT_CREATED_TIME + "," + UPDATED_CREATED_TIME);

        // Get all the withdrawDepositList where createdTime equals to UPDATED_CREATED_TIME
        defaultWithdrawDepositShouldNotBeFound("createdTime.in=" + UPDATED_CREATED_TIME);
    }

    @Test
    @Transactional
    public void getAllWithdrawDepositsByCreatedTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        withdrawDepositRepository.saveAndFlush(withdrawDeposit);

        // Get all the withdrawDepositList where createdTime is not null
        defaultWithdrawDepositShouldBeFound("createdTime.specified=true");

        // Get all the withdrawDepositList where createdTime is null
        defaultWithdrawDepositShouldNotBeFound("createdTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllWithdrawDepositsByUpdatedTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        withdrawDepositRepository.saveAndFlush(withdrawDeposit);

        // Get all the withdrawDepositList where updatedTime equals to DEFAULT_UPDATED_TIME
        defaultWithdrawDepositShouldBeFound("updatedTime.equals=" + DEFAULT_UPDATED_TIME);

        // Get all the withdrawDepositList where updatedTime equals to UPDATED_UPDATED_TIME
        defaultWithdrawDepositShouldNotBeFound("updatedTime.equals=" + UPDATED_UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllWithdrawDepositsByUpdatedTimeIsInShouldWork() throws Exception {
        // Initialize the database
        withdrawDepositRepository.saveAndFlush(withdrawDeposit);

        // Get all the withdrawDepositList where updatedTime in DEFAULT_UPDATED_TIME or UPDATED_UPDATED_TIME
        defaultWithdrawDepositShouldBeFound("updatedTime.in=" + DEFAULT_UPDATED_TIME + "," + UPDATED_UPDATED_TIME);

        // Get all the withdrawDepositList where updatedTime equals to UPDATED_UPDATED_TIME
        defaultWithdrawDepositShouldNotBeFound("updatedTime.in=" + UPDATED_UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllWithdrawDepositsByUpdatedTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        withdrawDepositRepository.saveAndFlush(withdrawDeposit);

        // Get all the withdrawDepositList where updatedTime is not null
        defaultWithdrawDepositShouldBeFound("updatedTime.specified=true");

        // Get all the withdrawDepositList where updatedTime is null
        defaultWithdrawDepositShouldNotBeFound("updatedTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllWithdrawDepositsByUseridIsEqualToSomething() throws Exception {
        // Initialize the database
        withdrawDepositRepository.saveAndFlush(withdrawDeposit);

        // Get all the withdrawDepositList where userid equals to DEFAULT_USERID
        defaultWithdrawDepositShouldBeFound("userid.equals=" + DEFAULT_USERID);

        // Get all the withdrawDepositList where userid equals to UPDATED_USERID
        defaultWithdrawDepositShouldNotBeFound("userid.equals=" + UPDATED_USERID);
    }

    @Test
    @Transactional
    public void getAllWithdrawDepositsByUseridIsInShouldWork() throws Exception {
        // Initialize the database
        withdrawDepositRepository.saveAndFlush(withdrawDeposit);

        // Get all the withdrawDepositList where userid in DEFAULT_USERID or UPDATED_USERID
        defaultWithdrawDepositShouldBeFound("userid.in=" + DEFAULT_USERID + "," + UPDATED_USERID);

        // Get all the withdrawDepositList where userid equals to UPDATED_USERID
        defaultWithdrawDepositShouldNotBeFound("userid.in=" + UPDATED_USERID);
    }

    @Test
    @Transactional
    public void getAllWithdrawDepositsByUseridIsNullOrNotNull() throws Exception {
        // Initialize the database
        withdrawDepositRepository.saveAndFlush(withdrawDeposit);

        // Get all the withdrawDepositList where userid is not null
        defaultWithdrawDepositShouldBeFound("userid.specified=true");

        // Get all the withdrawDepositList where userid is null
        defaultWithdrawDepositShouldNotBeFound("userid.specified=false");
    }

    @Test
    @Transactional
    public void getAllWithdrawDepositsByUseridIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        withdrawDepositRepository.saveAndFlush(withdrawDeposit);

        // Get all the withdrawDepositList where userid greater than or equals to DEFAULT_USERID
        defaultWithdrawDepositShouldBeFound("userid.greaterOrEqualThan=" + DEFAULT_USERID);

        // Get all the withdrawDepositList where userid greater than or equals to UPDATED_USERID
        defaultWithdrawDepositShouldNotBeFound("userid.greaterOrEqualThan=" + UPDATED_USERID);
    }

    @Test
    @Transactional
    public void getAllWithdrawDepositsByUseridIsLessThanSomething() throws Exception {
        // Initialize the database
        withdrawDepositRepository.saveAndFlush(withdrawDeposit);

        // Get all the withdrawDepositList where userid less than or equals to DEFAULT_USERID
        defaultWithdrawDepositShouldNotBeFound("userid.lessThan=" + DEFAULT_USERID);

        // Get all the withdrawDepositList where userid less than or equals to UPDATED_USERID
        defaultWithdrawDepositShouldBeFound("userid.lessThan=" + UPDATED_USERID);
    }


    @Test
    @Transactional
    public void getAllWithdrawDepositsByWalletIsEqualToSomething() throws Exception {
        // Initialize the database
        Wallet wallet = WalletResourceIntTest.createEntity(em);
        em.persist(wallet);
        em.flush();
        withdrawDeposit.setWallet(wallet);
        withdrawDepositRepository.saveAndFlush(withdrawDeposit);
        Long walletId = wallet.getId();

        // Get all the withdrawDepositList where wallet equals to walletId
        defaultWithdrawDepositShouldBeFound("walletId.equals=" + walletId);

        // Get all the withdrawDepositList where wallet equals to walletId + 1
        defaultWithdrawDepositShouldNotBeFound("walletId.equals=" + (walletId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultWithdrawDepositShouldBeFound(String filter) throws Exception {
        restWithdrawDepositMockMvc.perform(get("/api/withdraw-deposits?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(withdrawDeposit.getId().intValue())))
            .andExpect(jsonPath("$.[*].cardholder").value(hasItem(DEFAULT_CARDHOLDER.toString())))
            .andExpect(jsonPath("$.[*].openingBank").value(hasItem(DEFAULT_OPENING_BANK.toString())))
            .andExpect(jsonPath("$.[*].bankcardNumber").value(hasItem(DEFAULT_BANKCARD_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].money").value(hasItem(DEFAULT_MONEY.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].statusString").value(hasItem(DEFAULT_STATUS_STRING.toString())))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(DEFAULT_CREATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].updatedTime").value(hasItem(DEFAULT_UPDATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].userid").value(hasItem(DEFAULT_USERID.intValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultWithdrawDepositShouldNotBeFound(String filter) throws Exception {
        restWithdrawDepositMockMvc.perform(get("/api/withdraw-deposits?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingWithdrawDeposit() throws Exception {
        // Get the withdrawDeposit
        restWithdrawDepositMockMvc.perform(get("/api/withdraw-deposits/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWithdrawDeposit() throws Exception {
        // Initialize the database
        withdrawDepositRepository.saveAndFlush(withdrawDeposit);
        int databaseSizeBeforeUpdate = withdrawDepositRepository.findAll().size();

        // Update the withdrawDeposit
        WithdrawDeposit updatedWithdrawDeposit = withdrawDepositRepository.findOne(withdrawDeposit.getId());
        // Disconnect from session so that the updates on updatedWithdrawDeposit are not directly saved in db
        em.detach(updatedWithdrawDeposit);
        updatedWithdrawDeposit
            .cardholder(UPDATED_CARDHOLDER)
            .openingBank(UPDATED_OPENING_BANK)
            .bankcardNumber(UPDATED_BANKCARD_NUMBER)
            .money(UPDATED_MONEY)
            .status(UPDATED_STATUS)
            .statusString(UPDATED_STATUS_STRING)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .userid(UPDATED_USERID);
        WithdrawDepositDTO withdrawDepositDTO = withdrawDepositMapper.toDto(updatedWithdrawDeposit);

        restWithdrawDepositMockMvc.perform(put("/api/withdraw-deposits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(withdrawDepositDTO)))
            .andExpect(status().isOk());

        // Validate the WithdrawDeposit in the database
        List<WithdrawDeposit> withdrawDepositList = withdrawDepositRepository.findAll();
        assertThat(withdrawDepositList).hasSize(databaseSizeBeforeUpdate);
        WithdrawDeposit testWithdrawDeposit = withdrawDepositList.get(withdrawDepositList.size() - 1);
        assertThat(testWithdrawDeposit.getCardholder()).isEqualTo(UPDATED_CARDHOLDER);
        assertThat(testWithdrawDeposit.getOpeningBank()).isEqualTo(UPDATED_OPENING_BANK);
        assertThat(testWithdrawDeposit.getBankcardNumber()).isEqualTo(UPDATED_BANKCARD_NUMBER);
        assertThat(testWithdrawDeposit.getMoney()).isEqualTo(UPDATED_MONEY);
        assertThat(testWithdrawDeposit.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testWithdrawDeposit.getStatusString()).isEqualTo(UPDATED_STATUS_STRING);
        assertThat(testWithdrawDeposit.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testWithdrawDeposit.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testWithdrawDeposit.getUserid()).isEqualTo(UPDATED_USERID);
    }

    @Test
    @Transactional
    public void updateNonExistingWithdrawDeposit() throws Exception {
        int databaseSizeBeforeUpdate = withdrawDepositRepository.findAll().size();

        // Create the WithdrawDeposit
        WithdrawDepositDTO withdrawDepositDTO = withdrawDepositMapper.toDto(withdrawDeposit);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWithdrawDepositMockMvc.perform(put("/api/withdraw-deposits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(withdrawDepositDTO)))
            .andExpect(status().isCreated());

        // Validate the WithdrawDeposit in the database
        List<WithdrawDeposit> withdrawDepositList = withdrawDepositRepository.findAll();
        assertThat(withdrawDepositList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteWithdrawDeposit() throws Exception {
        // Initialize the database
        withdrawDepositRepository.saveAndFlush(withdrawDeposit);
        int databaseSizeBeforeDelete = withdrawDepositRepository.findAll().size();

        // Get the withdrawDeposit
        restWithdrawDepositMockMvc.perform(delete("/api/withdraw-deposits/{id}", withdrawDeposit.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<WithdrawDeposit> withdrawDepositList = withdrawDepositRepository.findAll();
        assertThat(withdrawDepositList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WithdrawDeposit.class);
        WithdrawDeposit withdrawDeposit1 = new WithdrawDeposit();
        withdrawDeposit1.setId(1L);
        WithdrawDeposit withdrawDeposit2 = new WithdrawDeposit();
        withdrawDeposit2.setId(withdrawDeposit1.getId());
        assertThat(withdrawDeposit1).isEqualTo(withdrawDeposit2);
        withdrawDeposit2.setId(2L);
        assertThat(withdrawDeposit1).isNotEqualTo(withdrawDeposit2);
        withdrawDeposit1.setId(null);
        assertThat(withdrawDeposit1).isNotEqualTo(withdrawDeposit2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WithdrawDepositDTO.class);
        WithdrawDepositDTO withdrawDepositDTO1 = new WithdrawDepositDTO();
        withdrawDepositDTO1.setId(1L);
        WithdrawDepositDTO withdrawDepositDTO2 = new WithdrawDepositDTO();
        assertThat(withdrawDepositDTO1).isNotEqualTo(withdrawDepositDTO2);
        withdrawDepositDTO2.setId(withdrawDepositDTO1.getId());
        assertThat(withdrawDepositDTO1).isEqualTo(withdrawDepositDTO2);
        withdrawDepositDTO2.setId(2L);
        assertThat(withdrawDepositDTO1).isNotEqualTo(withdrawDepositDTO2);
        withdrawDepositDTO1.setId(null);
        assertThat(withdrawDepositDTO1).isNotEqualTo(withdrawDepositDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(withdrawDepositMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(withdrawDepositMapper.fromId(null)).isNull();
    }
}
