package com.eyun.wallet.web.rest;

import com.eyun.wallet.WalletApp;

import com.eyun.wallet.config.SecurityBeanOverrideConfiguration;

import com.eyun.wallet.domain.Wallet;
import com.eyun.wallet.domain.BalanceDetails;
import com.eyun.wallet.domain.IntegralDetails;
import com.eyun.wallet.domain.TicketDetails;
import com.eyun.wallet.repository.WalletRepository;
import com.eyun.wallet.service.WalletService;
import com.eyun.wallet.service.dto.WalletDTO;
import com.eyun.wallet.service.mapper.WalletMapper;
import com.eyun.wallet.web.rest.errors.ExceptionTranslator;
import com.eyun.wallet.service.dto.WalletCriteria;
import com.eyun.wallet.service.WalletQueryService;

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
 * Test class for the WalletResource REST controller.
 *
 * @see WalletResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WalletApp.class, SecurityBeanOverrideConfiguration.class})
public class WalletResourceIntTest {

    private static final Long DEFAULT_USERID = 1L;
    private static final Long UPDATED_USERID = 2L;

    private static final Instant DEFAULT_CREATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_VERSION = 1;
    private static final Integer UPDATED_VERSION = 2;

    private static final BigDecimal DEFAULT_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_BALANCE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TICKET = new BigDecimal(1);
    private static final BigDecimal UPDATED_TICKET = new BigDecimal(2);

    private static final BigDecimal DEFAULT_INTEGRAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_INTEGRAL = new BigDecimal(2);

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WalletMapper walletMapper;

    @Autowired
    private WalletService walletService;

    @Autowired
    private WalletQueryService walletQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWalletMockMvc;

    private Wallet wallet;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WalletResource walletResource = new WalletResource(walletService, walletQueryService);
        this.restWalletMockMvc = MockMvcBuilders.standaloneSetup(walletResource)
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
    public static Wallet createEntity(EntityManager em) {
        Wallet wallet = new Wallet()
            .userid(DEFAULT_USERID)
            .createTime(DEFAULT_CREATE_TIME)
            .updatedTime(DEFAULT_UPDATED_TIME)
            .version(DEFAULT_VERSION)
            .balance(DEFAULT_BALANCE)
            .ticket(DEFAULT_TICKET)
            .integral(DEFAULT_INTEGRAL)
            .password(DEFAULT_PASSWORD);
        return wallet;
    }

    @Before
    public void initTest() {
        wallet = createEntity(em);
    }

    @Test
    @Transactional
    public void createWallet() throws Exception {
        int databaseSizeBeforeCreate = walletRepository.findAll().size();

        // Create the Wallet
        WalletDTO walletDTO = walletMapper.toDto(wallet);
        restWalletMockMvc.perform(post("/api/wallets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(walletDTO)))
            .andExpect(status().isCreated());

        // Validate the Wallet in the database
        List<Wallet> walletList = walletRepository.findAll();
        assertThat(walletList).hasSize(databaseSizeBeforeCreate + 1);
        Wallet testWallet = walletList.get(walletList.size() - 1);
        assertThat(testWallet.getUserid()).isEqualTo(DEFAULT_USERID);
        assertThat(testWallet.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testWallet.getUpdatedTime()).isEqualTo(DEFAULT_UPDATED_TIME);
        assertThat(testWallet.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testWallet.getBalance()).isEqualTo(DEFAULT_BALANCE);
        assertThat(testWallet.getTicket()).isEqualTo(DEFAULT_TICKET);
        assertThat(testWallet.getIntegral()).isEqualTo(DEFAULT_INTEGRAL);
        assertThat(testWallet.getPassword()).isEqualTo(DEFAULT_PASSWORD);
    }

    @Test
    @Transactional
    public void createWalletWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = walletRepository.findAll().size();

        // Create the Wallet with an existing ID
        wallet.setId(1L);
        WalletDTO walletDTO = walletMapper.toDto(wallet);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWalletMockMvc.perform(post("/api/wallets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(walletDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Wallet in the database
        List<Wallet> walletList = walletRepository.findAll();
        assertThat(walletList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkUseridIsRequired() throws Exception {
        int databaseSizeBeforeTest = walletRepository.findAll().size();
        // set the field null
        wallet.setUserid(null);

        // Create the Wallet, which fails.
        WalletDTO walletDTO = walletMapper.toDto(wallet);

        restWalletMockMvc.perform(post("/api/wallets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(walletDTO)))
            .andExpect(status().isBadRequest());

        List<Wallet> walletList = walletRepository.findAll();
        assertThat(walletList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWallets() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList
        restWalletMockMvc.perform(get("/api/wallets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wallet.getId().intValue())))
            .andExpect(jsonPath("$.[*].userid").value(hasItem(DEFAULT_USERID.intValue())))
            .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].updatedTime").value(hasItem(DEFAULT_UPDATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.intValue())))
            .andExpect(jsonPath("$.[*].ticket").value(hasItem(DEFAULT_TICKET.intValue())))
            .andExpect(jsonPath("$.[*].integral").value(hasItem(DEFAULT_INTEGRAL.intValue())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())));
    }

    @Test
    @Transactional
    public void getWallet() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get the wallet
        restWalletMockMvc.perform(get("/api/wallets/{id}", wallet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(wallet.getId().intValue()))
            .andExpect(jsonPath("$.userid").value(DEFAULT_USERID.intValue()))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.toString()))
            .andExpect(jsonPath("$.updatedTime").value(DEFAULT_UPDATED_TIME.toString()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.intValue()))
            .andExpect(jsonPath("$.ticket").value(DEFAULT_TICKET.intValue()))
            .andExpect(jsonPath("$.integral").value(DEFAULT_INTEGRAL.intValue()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()));
    }

    @Test
    @Transactional
    public void getAllWalletsByUseridIsEqualToSomething() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where userid equals to DEFAULT_USERID
        defaultWalletShouldBeFound("userid.equals=" + DEFAULT_USERID);

        // Get all the walletList where userid equals to UPDATED_USERID
        defaultWalletShouldNotBeFound("userid.equals=" + UPDATED_USERID);
    }

    @Test
    @Transactional
    public void getAllWalletsByUseridIsInShouldWork() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where userid in DEFAULT_USERID or UPDATED_USERID
        defaultWalletShouldBeFound("userid.in=" + DEFAULT_USERID + "," + UPDATED_USERID);

        // Get all the walletList where userid equals to UPDATED_USERID
        defaultWalletShouldNotBeFound("userid.in=" + UPDATED_USERID);
    }

    @Test
    @Transactional
    public void getAllWalletsByUseridIsNullOrNotNull() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where userid is not null
        defaultWalletShouldBeFound("userid.specified=true");

        // Get all the walletList where userid is null
        defaultWalletShouldNotBeFound("userid.specified=false");
    }

    @Test
    @Transactional
    public void getAllWalletsByUseridIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where userid greater than or equals to DEFAULT_USERID
        defaultWalletShouldBeFound("userid.greaterOrEqualThan=" + DEFAULT_USERID);

        // Get all the walletList where userid greater than or equals to UPDATED_USERID
        defaultWalletShouldNotBeFound("userid.greaterOrEqualThan=" + UPDATED_USERID);
    }

    @Test
    @Transactional
    public void getAllWalletsByUseridIsLessThanSomething() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where userid less than or equals to DEFAULT_USERID
        defaultWalletShouldNotBeFound("userid.lessThan=" + DEFAULT_USERID);

        // Get all the walletList where userid less than or equals to UPDATED_USERID
        defaultWalletShouldBeFound("userid.lessThan=" + UPDATED_USERID);
    }


    @Test
    @Transactional
    public void getAllWalletsByCreateTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where createTime equals to DEFAULT_CREATE_TIME
        defaultWalletShouldBeFound("createTime.equals=" + DEFAULT_CREATE_TIME);

        // Get all the walletList where createTime equals to UPDATED_CREATE_TIME
        defaultWalletShouldNotBeFound("createTime.equals=" + UPDATED_CREATE_TIME);
    }

    @Test
    @Transactional
    public void getAllWalletsByCreateTimeIsInShouldWork() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where createTime in DEFAULT_CREATE_TIME or UPDATED_CREATE_TIME
        defaultWalletShouldBeFound("createTime.in=" + DEFAULT_CREATE_TIME + "," + UPDATED_CREATE_TIME);

        // Get all the walletList where createTime equals to UPDATED_CREATE_TIME
        defaultWalletShouldNotBeFound("createTime.in=" + UPDATED_CREATE_TIME);
    }

    @Test
    @Transactional
    public void getAllWalletsByCreateTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where createTime is not null
        defaultWalletShouldBeFound("createTime.specified=true");

        // Get all the walletList where createTime is null
        defaultWalletShouldNotBeFound("createTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllWalletsByUpdatedTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where updatedTime equals to DEFAULT_UPDATED_TIME
        defaultWalletShouldBeFound("updatedTime.equals=" + DEFAULT_UPDATED_TIME);

        // Get all the walletList where updatedTime equals to UPDATED_UPDATED_TIME
        defaultWalletShouldNotBeFound("updatedTime.equals=" + UPDATED_UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllWalletsByUpdatedTimeIsInShouldWork() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where updatedTime in DEFAULT_UPDATED_TIME or UPDATED_UPDATED_TIME
        defaultWalletShouldBeFound("updatedTime.in=" + DEFAULT_UPDATED_TIME + "," + UPDATED_UPDATED_TIME);

        // Get all the walletList where updatedTime equals to UPDATED_UPDATED_TIME
        defaultWalletShouldNotBeFound("updatedTime.in=" + UPDATED_UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllWalletsByUpdatedTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where updatedTime is not null
        defaultWalletShouldBeFound("updatedTime.specified=true");

        // Get all the walletList where updatedTime is null
        defaultWalletShouldNotBeFound("updatedTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllWalletsByVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where version equals to DEFAULT_VERSION
        defaultWalletShouldBeFound("version.equals=" + DEFAULT_VERSION);

        // Get all the walletList where version equals to UPDATED_VERSION
        defaultWalletShouldNotBeFound("version.equals=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    public void getAllWalletsByVersionIsInShouldWork() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where version in DEFAULT_VERSION or UPDATED_VERSION
        defaultWalletShouldBeFound("version.in=" + DEFAULT_VERSION + "," + UPDATED_VERSION);

        // Get all the walletList where version equals to UPDATED_VERSION
        defaultWalletShouldNotBeFound("version.in=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    public void getAllWalletsByVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where version is not null
        defaultWalletShouldBeFound("version.specified=true");

        // Get all the walletList where version is null
        defaultWalletShouldNotBeFound("version.specified=false");
    }

    @Test
    @Transactional
    public void getAllWalletsByVersionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where version greater than or equals to DEFAULT_VERSION
        defaultWalletShouldBeFound("version.greaterOrEqualThan=" + DEFAULT_VERSION);

        // Get all the walletList where version greater than or equals to UPDATED_VERSION
        defaultWalletShouldNotBeFound("version.greaterOrEqualThan=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    public void getAllWalletsByVersionIsLessThanSomething() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where version less than or equals to DEFAULT_VERSION
        defaultWalletShouldNotBeFound("version.lessThan=" + DEFAULT_VERSION);

        // Get all the walletList where version less than or equals to UPDATED_VERSION
        defaultWalletShouldBeFound("version.lessThan=" + UPDATED_VERSION);
    }


    @Test
    @Transactional
    public void getAllWalletsByBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where balance equals to DEFAULT_BALANCE
        defaultWalletShouldBeFound("balance.equals=" + DEFAULT_BALANCE);

        // Get all the walletList where balance equals to UPDATED_BALANCE
        defaultWalletShouldNotBeFound("balance.equals=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllWalletsByBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where balance in DEFAULT_BALANCE or UPDATED_BALANCE
        defaultWalletShouldBeFound("balance.in=" + DEFAULT_BALANCE + "," + UPDATED_BALANCE);

        // Get all the walletList where balance equals to UPDATED_BALANCE
        defaultWalletShouldNotBeFound("balance.in=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllWalletsByBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where balance is not null
        defaultWalletShouldBeFound("balance.specified=true");

        // Get all the walletList where balance is null
        defaultWalletShouldNotBeFound("balance.specified=false");
    }

    @Test
    @Transactional
    public void getAllWalletsByTicketIsEqualToSomething() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where ticket equals to DEFAULT_TICKET
        defaultWalletShouldBeFound("ticket.equals=" + DEFAULT_TICKET);

        // Get all the walletList where ticket equals to UPDATED_TICKET
        defaultWalletShouldNotBeFound("ticket.equals=" + UPDATED_TICKET);
    }

    @Test
    @Transactional
    public void getAllWalletsByTicketIsInShouldWork() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where ticket in DEFAULT_TICKET or UPDATED_TICKET
        defaultWalletShouldBeFound("ticket.in=" + DEFAULT_TICKET + "," + UPDATED_TICKET);

        // Get all the walletList where ticket equals to UPDATED_TICKET
        defaultWalletShouldNotBeFound("ticket.in=" + UPDATED_TICKET);
    }

    @Test
    @Transactional
    public void getAllWalletsByTicketIsNullOrNotNull() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where ticket is not null
        defaultWalletShouldBeFound("ticket.specified=true");

        // Get all the walletList where ticket is null
        defaultWalletShouldNotBeFound("ticket.specified=false");
    }

    @Test
    @Transactional
    public void getAllWalletsByIntegralIsEqualToSomething() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where integral equals to DEFAULT_INTEGRAL
        defaultWalletShouldBeFound("integral.equals=" + DEFAULT_INTEGRAL);

        // Get all the walletList where integral equals to UPDATED_INTEGRAL
        defaultWalletShouldNotBeFound("integral.equals=" + UPDATED_INTEGRAL);
    }

    @Test
    @Transactional
    public void getAllWalletsByIntegralIsInShouldWork() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where integral in DEFAULT_INTEGRAL or UPDATED_INTEGRAL
        defaultWalletShouldBeFound("integral.in=" + DEFAULT_INTEGRAL + "," + UPDATED_INTEGRAL);

        // Get all the walletList where integral equals to UPDATED_INTEGRAL
        defaultWalletShouldNotBeFound("integral.in=" + UPDATED_INTEGRAL);
    }

    @Test
    @Transactional
    public void getAllWalletsByIntegralIsNullOrNotNull() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where integral is not null
        defaultWalletShouldBeFound("integral.specified=true");

        // Get all the walletList where integral is null
        defaultWalletShouldNotBeFound("integral.specified=false");
    }

    @Test
    @Transactional
    public void getAllWalletsByPasswordIsEqualToSomething() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where password equals to DEFAULT_PASSWORD
        defaultWalletShouldBeFound("password.equals=" + DEFAULT_PASSWORD);

        // Get all the walletList where password equals to UPDATED_PASSWORD
        defaultWalletShouldNotBeFound("password.equals=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    public void getAllWalletsByPasswordIsInShouldWork() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where password in DEFAULT_PASSWORD or UPDATED_PASSWORD
        defaultWalletShouldBeFound("password.in=" + DEFAULT_PASSWORD + "," + UPDATED_PASSWORD);

        // Get all the walletList where password equals to UPDATED_PASSWORD
        defaultWalletShouldNotBeFound("password.in=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    public void getAllWalletsByPasswordIsNullOrNotNull() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where password is not null
        defaultWalletShouldBeFound("password.specified=true");

        // Get all the walletList where password is null
        defaultWalletShouldNotBeFound("password.specified=false");
    }

    @Test
    @Transactional
    public void getAllWalletsByBalanceDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        BalanceDetails balanceDetails = BalanceDetailsResourceIntTest.createEntity(em);
        em.persist(balanceDetails);
        em.flush();
        wallet.addBalanceDetails(balanceDetails);
        walletRepository.saveAndFlush(wallet);
        Long balanceDetailsId = balanceDetails.getId();

        // Get all the walletList where balanceDetails equals to balanceDetailsId
        defaultWalletShouldBeFound("balanceDetailsId.equals=" + balanceDetailsId);

        // Get all the walletList where balanceDetails equals to balanceDetailsId + 1
        defaultWalletShouldNotBeFound("balanceDetailsId.equals=" + (balanceDetailsId + 1));
    }


    @Test
    @Transactional
    public void getAllWalletsByIntegralDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        IntegralDetails integralDetails = IntegralDetailsResourceIntTest.createEntity(em);
        em.persist(integralDetails);
        em.flush();
        wallet.addIntegralDetails(integralDetails);
        walletRepository.saveAndFlush(wallet);
        Long integralDetailsId = integralDetails.getId();

        // Get all the walletList where integralDetails equals to integralDetailsId
        defaultWalletShouldBeFound("integralDetailsId.equals=" + integralDetailsId);

        // Get all the walletList where integralDetails equals to integralDetailsId + 1
        defaultWalletShouldNotBeFound("integralDetailsId.equals=" + (integralDetailsId + 1));
    }


    @Test
    @Transactional
    public void getAllWalletsByTicketDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        TicketDetails ticketDetails = TicketDetailsResourceIntTest.createEntity(em);
        em.persist(ticketDetails);
        em.flush();
        wallet.addTicketDetails(ticketDetails);
        walletRepository.saveAndFlush(wallet);
        Long ticketDetailsId = ticketDetails.getId();

        // Get all the walletList where ticketDetails equals to ticketDetailsId
        defaultWalletShouldBeFound("ticketDetailsId.equals=" + ticketDetailsId);

        // Get all the walletList where ticketDetails equals to ticketDetailsId + 1
        defaultWalletShouldNotBeFound("ticketDetailsId.equals=" + (ticketDetailsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultWalletShouldBeFound(String filter) throws Exception {
        restWalletMockMvc.perform(get("/api/wallets?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wallet.getId().intValue())))
            .andExpect(jsonPath("$.[*].userid").value(hasItem(DEFAULT_USERID.intValue())))
            .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].updatedTime").value(hasItem(DEFAULT_UPDATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.intValue())))
            .andExpect(jsonPath("$.[*].ticket").value(hasItem(DEFAULT_TICKET.intValue())))
            .andExpect(jsonPath("$.[*].integral").value(hasItem(DEFAULT_INTEGRAL.intValue())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultWalletShouldNotBeFound(String filter) throws Exception {
        restWalletMockMvc.perform(get("/api/wallets?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingWallet() throws Exception {
        // Get the wallet
        restWalletMockMvc.perform(get("/api/wallets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWallet() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);
        int databaseSizeBeforeUpdate = walletRepository.findAll().size();

        // Update the wallet
        Wallet updatedWallet = walletRepository.findOne(wallet.getId());
        // Disconnect from session so that the updates on updatedWallet are not directly saved in db
        em.detach(updatedWallet);
        updatedWallet
            .userid(UPDATED_USERID)
            .createTime(UPDATED_CREATE_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .version(UPDATED_VERSION)
            .balance(UPDATED_BALANCE)
            .ticket(UPDATED_TICKET)
            .integral(UPDATED_INTEGRAL)
            .password(UPDATED_PASSWORD);
        WalletDTO walletDTO = walletMapper.toDto(updatedWallet);

        restWalletMockMvc.perform(put("/api/wallets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(walletDTO)))
            .andExpect(status().isOk());

        // Validate the Wallet in the database
        List<Wallet> walletList = walletRepository.findAll();
        assertThat(walletList).hasSize(databaseSizeBeforeUpdate);
        Wallet testWallet = walletList.get(walletList.size() - 1);
        assertThat(testWallet.getUserid()).isEqualTo(UPDATED_USERID);
        assertThat(testWallet.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testWallet.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testWallet.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testWallet.getBalance()).isEqualTo(UPDATED_BALANCE);
        assertThat(testWallet.getTicket()).isEqualTo(UPDATED_TICKET);
        assertThat(testWallet.getIntegral()).isEqualTo(UPDATED_INTEGRAL);
        assertThat(testWallet.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    public void updateNonExistingWallet() throws Exception {
        int databaseSizeBeforeUpdate = walletRepository.findAll().size();

        // Create the Wallet
        WalletDTO walletDTO = walletMapper.toDto(wallet);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWalletMockMvc.perform(put("/api/wallets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(walletDTO)))
            .andExpect(status().isCreated());

        // Validate the Wallet in the database
        List<Wallet> walletList = walletRepository.findAll();
        assertThat(walletList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteWallet() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);
        int databaseSizeBeforeDelete = walletRepository.findAll().size();

        // Get the wallet
        restWalletMockMvc.perform(delete("/api/wallets/{id}", wallet.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Wallet> walletList = walletRepository.findAll();
        assertThat(walletList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Wallet.class);
        Wallet wallet1 = new Wallet();
        wallet1.setId(1L);
        Wallet wallet2 = new Wallet();
        wallet2.setId(wallet1.getId());
        assertThat(wallet1).isEqualTo(wallet2);
        wallet2.setId(2L);
        assertThat(wallet1).isNotEqualTo(wallet2);
        wallet1.setId(null);
        assertThat(wallet1).isNotEqualTo(wallet2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WalletDTO.class);
        WalletDTO walletDTO1 = new WalletDTO();
        walletDTO1.setId(1L);
        WalletDTO walletDTO2 = new WalletDTO();
        assertThat(walletDTO1).isNotEqualTo(walletDTO2);
        walletDTO2.setId(walletDTO1.getId());
        assertThat(walletDTO1).isEqualTo(walletDTO2);
        walletDTO2.setId(2L);
        assertThat(walletDTO1).isNotEqualTo(walletDTO2);
        walletDTO1.setId(null);
        assertThat(walletDTO1).isNotEqualTo(walletDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(walletMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(walletMapper.fromId(null)).isNull();
    }
}
