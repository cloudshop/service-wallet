package com.eyun.wallet.web.rest;

import com.eyun.wallet.WalletApp;

import com.eyun.wallet.config.SecurityBeanOverrideConfiguration;

import com.eyun.wallet.domain.BalanceDetails;
import com.eyun.wallet.domain.Wallet;
import com.eyun.wallet.repository.BalanceDetailsRepository;
import com.eyun.wallet.service.BalanceDetailsService;
import com.eyun.wallet.service.dto.BalanceDetailsDTO;
import com.eyun.wallet.service.mapper.BalanceDetailsMapper;
import com.eyun.wallet.web.rest.errors.ExceptionTranslator;
import com.eyun.wallet.service.dto.BalanceDetailsCriteria;
import com.eyun.wallet.service.BalanceDetailsQueryService;

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
 * Test class for the BalanceDetailsResource REST controller.
 *
 * @see BalanceDetailsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WalletApp.class, SecurityBeanOverrideConfiguration.class})
public class BalanceDetailsResourceIntTest {

    private static final Long DEFAULT_USERID = 1L;
    private static final Long UPDATED_USERID = 2L;

    private static final BigDecimal DEFAULT_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_BALANCE = new BigDecimal(2);

    private static final Boolean DEFAULT_ADD_BALANCE = false;
    private static final Boolean UPDATED_ADD_BALANCE = true;

    private static final Integer DEFAULT_TYPE = 1;
    private static final Integer UPDATED_TYPE = 2;

    private static final String DEFAULT_TYPE_STRING = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_STRING = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ORDER_NO = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_NO = "BBBBBBBBBB";

    @Autowired
    private BalanceDetailsRepository balanceDetailsRepository;

    @Autowired
    private BalanceDetailsMapper balanceDetailsMapper;

    @Autowired
    private BalanceDetailsService balanceDetailsService;

    @Autowired
    private BalanceDetailsQueryService balanceDetailsQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBalanceDetailsMockMvc;

    private BalanceDetails balanceDetails;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BalanceDetailsResource balanceDetailsResource = new BalanceDetailsResource(balanceDetailsService, balanceDetailsQueryService);
        this.restBalanceDetailsMockMvc = MockMvcBuilders.standaloneSetup(balanceDetailsResource)
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
    public static BalanceDetails createEntity(EntityManager em) {
        BalanceDetails balanceDetails = new BalanceDetails()
            .userid(DEFAULT_USERID)
            .balance(DEFAULT_BALANCE)
            .addBalance(DEFAULT_ADD_BALANCE)
            .type(DEFAULT_TYPE)
            .typeString(DEFAULT_TYPE_STRING)
            .createdTime(DEFAULT_CREATED_TIME)
            .orderNo(DEFAULT_ORDER_NO);
        return balanceDetails;
    }

    @Before
    public void initTest() {
        balanceDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createBalanceDetails() throws Exception {
        int databaseSizeBeforeCreate = balanceDetailsRepository.findAll().size();

        // Create the BalanceDetails
        BalanceDetailsDTO balanceDetailsDTO = balanceDetailsMapper.toDto(balanceDetails);
        restBalanceDetailsMockMvc.perform(post("/api/balance-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(balanceDetailsDTO)))
            .andExpect(status().isCreated());

        // Validate the BalanceDetails in the database
        List<BalanceDetails> balanceDetailsList = balanceDetailsRepository.findAll();
        assertThat(balanceDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        BalanceDetails testBalanceDetails = balanceDetailsList.get(balanceDetailsList.size() - 1);
        assertThat(testBalanceDetails.getUserid()).isEqualTo(DEFAULT_USERID);
        assertThat(testBalanceDetails.getBalance()).isEqualTo(DEFAULT_BALANCE);
        assertThat(testBalanceDetails.isAddBalance()).isEqualTo(DEFAULT_ADD_BALANCE);
        assertThat(testBalanceDetails.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testBalanceDetails.getTypeString()).isEqualTo(DEFAULT_TYPE_STRING);
        assertThat(testBalanceDetails.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testBalanceDetails.getOrderNo()).isEqualTo(DEFAULT_ORDER_NO);
    }

    @Test
    @Transactional
    public void createBalanceDetailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = balanceDetailsRepository.findAll().size();

        // Create the BalanceDetails with an existing ID
        balanceDetails.setId(1L);
        BalanceDetailsDTO balanceDetailsDTO = balanceDetailsMapper.toDto(balanceDetails);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBalanceDetailsMockMvc.perform(post("/api/balance-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(balanceDetailsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BalanceDetails in the database
        List<BalanceDetails> balanceDetailsList = balanceDetailsRepository.findAll();
        assertThat(balanceDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllBalanceDetails() throws Exception {
        // Initialize the database
        balanceDetailsRepository.saveAndFlush(balanceDetails);

        // Get all the balanceDetailsList
        restBalanceDetailsMockMvc.perform(get("/api/balance-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(balanceDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].userid").value(hasItem(DEFAULT_USERID.intValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.intValue())))
            .andExpect(jsonPath("$.[*].addBalance").value(hasItem(DEFAULT_ADD_BALANCE.booleanValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].typeString").value(hasItem(DEFAULT_TYPE_STRING.toString())))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(DEFAULT_CREATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].orderNo").value(hasItem(DEFAULT_ORDER_NO.toString())));
    }

    @Test
    @Transactional
    public void getBalanceDetails() throws Exception {
        // Initialize the database
        balanceDetailsRepository.saveAndFlush(balanceDetails);

        // Get the balanceDetails
        restBalanceDetailsMockMvc.perform(get("/api/balance-details/{id}", balanceDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(balanceDetails.getId().intValue()))
            .andExpect(jsonPath("$.userid").value(DEFAULT_USERID.intValue()))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.intValue()))
            .andExpect(jsonPath("$.addBalance").value(DEFAULT_ADD_BALANCE.booleanValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.typeString").value(DEFAULT_TYPE_STRING.toString()))
            .andExpect(jsonPath("$.createdTime").value(DEFAULT_CREATED_TIME.toString()))
            .andExpect(jsonPath("$.orderNo").value(DEFAULT_ORDER_NO.toString()));
    }

    @Test
    @Transactional
    public void getAllBalanceDetailsByUseridIsEqualToSomething() throws Exception {
        // Initialize the database
        balanceDetailsRepository.saveAndFlush(balanceDetails);

        // Get all the balanceDetailsList where userid equals to DEFAULT_USERID
        defaultBalanceDetailsShouldBeFound("userid.equals=" + DEFAULT_USERID);

        // Get all the balanceDetailsList where userid equals to UPDATED_USERID
        defaultBalanceDetailsShouldNotBeFound("userid.equals=" + UPDATED_USERID);
    }

    @Test
    @Transactional
    public void getAllBalanceDetailsByUseridIsInShouldWork() throws Exception {
        // Initialize the database
        balanceDetailsRepository.saveAndFlush(balanceDetails);

        // Get all the balanceDetailsList where userid in DEFAULT_USERID or UPDATED_USERID
        defaultBalanceDetailsShouldBeFound("userid.in=" + DEFAULT_USERID + "," + UPDATED_USERID);

        // Get all the balanceDetailsList where userid equals to UPDATED_USERID
        defaultBalanceDetailsShouldNotBeFound("userid.in=" + UPDATED_USERID);
    }

    @Test
    @Transactional
    public void getAllBalanceDetailsByUseridIsNullOrNotNull() throws Exception {
        // Initialize the database
        balanceDetailsRepository.saveAndFlush(balanceDetails);

        // Get all the balanceDetailsList where userid is not null
        defaultBalanceDetailsShouldBeFound("userid.specified=true");

        // Get all the balanceDetailsList where userid is null
        defaultBalanceDetailsShouldNotBeFound("userid.specified=false");
    }

    @Test
    @Transactional
    public void getAllBalanceDetailsByUseridIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        balanceDetailsRepository.saveAndFlush(balanceDetails);

        // Get all the balanceDetailsList where userid greater than or equals to DEFAULT_USERID
        defaultBalanceDetailsShouldBeFound("userid.greaterOrEqualThan=" + DEFAULT_USERID);

        // Get all the balanceDetailsList where userid greater than or equals to UPDATED_USERID
        defaultBalanceDetailsShouldNotBeFound("userid.greaterOrEqualThan=" + UPDATED_USERID);
    }

    @Test
    @Transactional
    public void getAllBalanceDetailsByUseridIsLessThanSomething() throws Exception {
        // Initialize the database
        balanceDetailsRepository.saveAndFlush(balanceDetails);

        // Get all the balanceDetailsList where userid less than or equals to DEFAULT_USERID
        defaultBalanceDetailsShouldNotBeFound("userid.lessThan=" + DEFAULT_USERID);

        // Get all the balanceDetailsList where userid less than or equals to UPDATED_USERID
        defaultBalanceDetailsShouldBeFound("userid.lessThan=" + UPDATED_USERID);
    }


    @Test
    @Transactional
    public void getAllBalanceDetailsByBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        balanceDetailsRepository.saveAndFlush(balanceDetails);

        // Get all the balanceDetailsList where balance equals to DEFAULT_BALANCE
        defaultBalanceDetailsShouldBeFound("balance.equals=" + DEFAULT_BALANCE);

        // Get all the balanceDetailsList where balance equals to UPDATED_BALANCE
        defaultBalanceDetailsShouldNotBeFound("balance.equals=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllBalanceDetailsByBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        balanceDetailsRepository.saveAndFlush(balanceDetails);

        // Get all the balanceDetailsList where balance in DEFAULT_BALANCE or UPDATED_BALANCE
        defaultBalanceDetailsShouldBeFound("balance.in=" + DEFAULT_BALANCE + "," + UPDATED_BALANCE);

        // Get all the balanceDetailsList where balance equals to UPDATED_BALANCE
        defaultBalanceDetailsShouldNotBeFound("balance.in=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllBalanceDetailsByBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        balanceDetailsRepository.saveAndFlush(balanceDetails);

        // Get all the balanceDetailsList where balance is not null
        defaultBalanceDetailsShouldBeFound("balance.specified=true");

        // Get all the balanceDetailsList where balance is null
        defaultBalanceDetailsShouldNotBeFound("balance.specified=false");
    }

    @Test
    @Transactional
    public void getAllBalanceDetailsByAddBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        balanceDetailsRepository.saveAndFlush(balanceDetails);

        // Get all the balanceDetailsList where addBalance equals to DEFAULT_ADD_BALANCE
        defaultBalanceDetailsShouldBeFound("addBalance.equals=" + DEFAULT_ADD_BALANCE);

        // Get all the balanceDetailsList where addBalance equals to UPDATED_ADD_BALANCE
        defaultBalanceDetailsShouldNotBeFound("addBalance.equals=" + UPDATED_ADD_BALANCE);
    }

    @Test
    @Transactional
    public void getAllBalanceDetailsByAddBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        balanceDetailsRepository.saveAndFlush(balanceDetails);

        // Get all the balanceDetailsList where addBalance in DEFAULT_ADD_BALANCE or UPDATED_ADD_BALANCE
        defaultBalanceDetailsShouldBeFound("addBalance.in=" + DEFAULT_ADD_BALANCE + "," + UPDATED_ADD_BALANCE);

        // Get all the balanceDetailsList where addBalance equals to UPDATED_ADD_BALANCE
        defaultBalanceDetailsShouldNotBeFound("addBalance.in=" + UPDATED_ADD_BALANCE);
    }

    @Test
    @Transactional
    public void getAllBalanceDetailsByAddBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        balanceDetailsRepository.saveAndFlush(balanceDetails);

        // Get all the balanceDetailsList where addBalance is not null
        defaultBalanceDetailsShouldBeFound("addBalance.specified=true");

        // Get all the balanceDetailsList where addBalance is null
        defaultBalanceDetailsShouldNotBeFound("addBalance.specified=false");
    }

    @Test
    @Transactional
    public void getAllBalanceDetailsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        balanceDetailsRepository.saveAndFlush(balanceDetails);

        // Get all the balanceDetailsList where type equals to DEFAULT_TYPE
        defaultBalanceDetailsShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the balanceDetailsList where type equals to UPDATED_TYPE
        defaultBalanceDetailsShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllBalanceDetailsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        balanceDetailsRepository.saveAndFlush(balanceDetails);

        // Get all the balanceDetailsList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultBalanceDetailsShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the balanceDetailsList where type equals to UPDATED_TYPE
        defaultBalanceDetailsShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllBalanceDetailsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        balanceDetailsRepository.saveAndFlush(balanceDetails);

        // Get all the balanceDetailsList where type is not null
        defaultBalanceDetailsShouldBeFound("type.specified=true");

        // Get all the balanceDetailsList where type is null
        defaultBalanceDetailsShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllBalanceDetailsByTypeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        balanceDetailsRepository.saveAndFlush(balanceDetails);

        // Get all the balanceDetailsList where type greater than or equals to DEFAULT_TYPE
        defaultBalanceDetailsShouldBeFound("type.greaterOrEqualThan=" + DEFAULT_TYPE);

        // Get all the balanceDetailsList where type greater than or equals to UPDATED_TYPE
        defaultBalanceDetailsShouldNotBeFound("type.greaterOrEqualThan=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllBalanceDetailsByTypeIsLessThanSomething() throws Exception {
        // Initialize the database
        balanceDetailsRepository.saveAndFlush(balanceDetails);

        // Get all the balanceDetailsList where type less than or equals to DEFAULT_TYPE
        defaultBalanceDetailsShouldNotBeFound("type.lessThan=" + DEFAULT_TYPE);

        // Get all the balanceDetailsList where type less than or equals to UPDATED_TYPE
        defaultBalanceDetailsShouldBeFound("type.lessThan=" + UPDATED_TYPE);
    }


    @Test
    @Transactional
    public void getAllBalanceDetailsByTypeStringIsEqualToSomething() throws Exception {
        // Initialize the database
        balanceDetailsRepository.saveAndFlush(balanceDetails);

        // Get all the balanceDetailsList where typeString equals to DEFAULT_TYPE_STRING
        defaultBalanceDetailsShouldBeFound("typeString.equals=" + DEFAULT_TYPE_STRING);

        // Get all the balanceDetailsList where typeString equals to UPDATED_TYPE_STRING
        defaultBalanceDetailsShouldNotBeFound("typeString.equals=" + UPDATED_TYPE_STRING);
    }

    @Test
    @Transactional
    public void getAllBalanceDetailsByTypeStringIsInShouldWork() throws Exception {
        // Initialize the database
        balanceDetailsRepository.saveAndFlush(balanceDetails);

        // Get all the balanceDetailsList where typeString in DEFAULT_TYPE_STRING or UPDATED_TYPE_STRING
        defaultBalanceDetailsShouldBeFound("typeString.in=" + DEFAULT_TYPE_STRING + "," + UPDATED_TYPE_STRING);

        // Get all the balanceDetailsList where typeString equals to UPDATED_TYPE_STRING
        defaultBalanceDetailsShouldNotBeFound("typeString.in=" + UPDATED_TYPE_STRING);
    }

    @Test
    @Transactional
    public void getAllBalanceDetailsByTypeStringIsNullOrNotNull() throws Exception {
        // Initialize the database
        balanceDetailsRepository.saveAndFlush(balanceDetails);

        // Get all the balanceDetailsList where typeString is not null
        defaultBalanceDetailsShouldBeFound("typeString.specified=true");

        // Get all the balanceDetailsList where typeString is null
        defaultBalanceDetailsShouldNotBeFound("typeString.specified=false");
    }

    @Test
    @Transactional
    public void getAllBalanceDetailsByCreatedTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        balanceDetailsRepository.saveAndFlush(balanceDetails);

        // Get all the balanceDetailsList where createdTime equals to DEFAULT_CREATED_TIME
        defaultBalanceDetailsShouldBeFound("createdTime.equals=" + DEFAULT_CREATED_TIME);

        // Get all the balanceDetailsList where createdTime equals to UPDATED_CREATED_TIME
        defaultBalanceDetailsShouldNotBeFound("createdTime.equals=" + UPDATED_CREATED_TIME);
    }

    @Test
    @Transactional
    public void getAllBalanceDetailsByCreatedTimeIsInShouldWork() throws Exception {
        // Initialize the database
        balanceDetailsRepository.saveAndFlush(balanceDetails);

        // Get all the balanceDetailsList where createdTime in DEFAULT_CREATED_TIME or UPDATED_CREATED_TIME
        defaultBalanceDetailsShouldBeFound("createdTime.in=" + DEFAULT_CREATED_TIME + "," + UPDATED_CREATED_TIME);

        // Get all the balanceDetailsList where createdTime equals to UPDATED_CREATED_TIME
        defaultBalanceDetailsShouldNotBeFound("createdTime.in=" + UPDATED_CREATED_TIME);
    }

    @Test
    @Transactional
    public void getAllBalanceDetailsByCreatedTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        balanceDetailsRepository.saveAndFlush(balanceDetails);

        // Get all the balanceDetailsList where createdTime is not null
        defaultBalanceDetailsShouldBeFound("createdTime.specified=true");

        // Get all the balanceDetailsList where createdTime is null
        defaultBalanceDetailsShouldNotBeFound("createdTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllBalanceDetailsByOrderNoIsEqualToSomething() throws Exception {
        // Initialize the database
        balanceDetailsRepository.saveAndFlush(balanceDetails);

        // Get all the balanceDetailsList where orderNo equals to DEFAULT_ORDER_NO
        defaultBalanceDetailsShouldBeFound("orderNo.equals=" + DEFAULT_ORDER_NO);

        // Get all the balanceDetailsList where orderNo equals to UPDATED_ORDER_NO
        defaultBalanceDetailsShouldNotBeFound("orderNo.equals=" + UPDATED_ORDER_NO);
    }

    @Test
    @Transactional
    public void getAllBalanceDetailsByOrderNoIsInShouldWork() throws Exception {
        // Initialize the database
        balanceDetailsRepository.saveAndFlush(balanceDetails);

        // Get all the balanceDetailsList where orderNo in DEFAULT_ORDER_NO or UPDATED_ORDER_NO
        defaultBalanceDetailsShouldBeFound("orderNo.in=" + DEFAULT_ORDER_NO + "," + UPDATED_ORDER_NO);

        // Get all the balanceDetailsList where orderNo equals to UPDATED_ORDER_NO
        defaultBalanceDetailsShouldNotBeFound("orderNo.in=" + UPDATED_ORDER_NO);
    }

    @Test
    @Transactional
    public void getAllBalanceDetailsByOrderNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        balanceDetailsRepository.saveAndFlush(balanceDetails);

        // Get all the balanceDetailsList where orderNo is not null
        defaultBalanceDetailsShouldBeFound("orderNo.specified=true");

        // Get all the balanceDetailsList where orderNo is null
        defaultBalanceDetailsShouldNotBeFound("orderNo.specified=false");
    }

    @Test
    @Transactional
    public void getAllBalanceDetailsByWalletIsEqualToSomething() throws Exception {
        // Initialize the database
        Wallet wallet = WalletResourceIntTest.createEntity(em);
        em.persist(wallet);
        em.flush();
        balanceDetails.setWallet(wallet);
        balanceDetailsRepository.saveAndFlush(balanceDetails);
        Long walletId = wallet.getId();

        // Get all the balanceDetailsList where wallet equals to walletId
        defaultBalanceDetailsShouldBeFound("walletId.equals=" + walletId);

        // Get all the balanceDetailsList where wallet equals to walletId + 1
        defaultBalanceDetailsShouldNotBeFound("walletId.equals=" + (walletId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultBalanceDetailsShouldBeFound(String filter) throws Exception {
        restBalanceDetailsMockMvc.perform(get("/api/balance-details?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(balanceDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].userid").value(hasItem(DEFAULT_USERID.intValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.intValue())))
            .andExpect(jsonPath("$.[*].addBalance").value(hasItem(DEFAULT_ADD_BALANCE.booleanValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].typeString").value(hasItem(DEFAULT_TYPE_STRING.toString())))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(DEFAULT_CREATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].orderNo").value(hasItem(DEFAULT_ORDER_NO.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultBalanceDetailsShouldNotBeFound(String filter) throws Exception {
        restBalanceDetailsMockMvc.perform(get("/api/balance-details?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingBalanceDetails() throws Exception {
        // Get the balanceDetails
        restBalanceDetailsMockMvc.perform(get("/api/balance-details/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBalanceDetails() throws Exception {
        // Initialize the database
        balanceDetailsRepository.saveAndFlush(balanceDetails);
        int databaseSizeBeforeUpdate = balanceDetailsRepository.findAll().size();

        // Update the balanceDetails
        BalanceDetails updatedBalanceDetails = balanceDetailsRepository.findOne(balanceDetails.getId());
        // Disconnect from session so that the updates on updatedBalanceDetails are not directly saved in db
        em.detach(updatedBalanceDetails);
        updatedBalanceDetails
            .userid(UPDATED_USERID)
            .balance(UPDATED_BALANCE)
            .addBalance(UPDATED_ADD_BALANCE)
            .type(UPDATED_TYPE)
            .typeString(UPDATED_TYPE_STRING)
            .createdTime(UPDATED_CREATED_TIME)
            .orderNo(UPDATED_ORDER_NO);
        BalanceDetailsDTO balanceDetailsDTO = balanceDetailsMapper.toDto(updatedBalanceDetails);

        restBalanceDetailsMockMvc.perform(put("/api/balance-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(balanceDetailsDTO)))
            .andExpect(status().isOk());

        // Validate the BalanceDetails in the database
        List<BalanceDetails> balanceDetailsList = balanceDetailsRepository.findAll();
        assertThat(balanceDetailsList).hasSize(databaseSizeBeforeUpdate);
        BalanceDetails testBalanceDetails = balanceDetailsList.get(balanceDetailsList.size() - 1);
        assertThat(testBalanceDetails.getUserid()).isEqualTo(UPDATED_USERID);
        assertThat(testBalanceDetails.getBalance()).isEqualTo(UPDATED_BALANCE);
        assertThat(testBalanceDetails.isAddBalance()).isEqualTo(UPDATED_ADD_BALANCE);
        assertThat(testBalanceDetails.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testBalanceDetails.getTypeString()).isEqualTo(UPDATED_TYPE_STRING);
        assertThat(testBalanceDetails.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testBalanceDetails.getOrderNo()).isEqualTo(UPDATED_ORDER_NO);
    }

    @Test
    @Transactional
    public void updateNonExistingBalanceDetails() throws Exception {
        int databaseSizeBeforeUpdate = balanceDetailsRepository.findAll().size();

        // Create the BalanceDetails
        BalanceDetailsDTO balanceDetailsDTO = balanceDetailsMapper.toDto(balanceDetails);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBalanceDetailsMockMvc.perform(put("/api/balance-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(balanceDetailsDTO)))
            .andExpect(status().isCreated());

        // Validate the BalanceDetails in the database
        List<BalanceDetails> balanceDetailsList = balanceDetailsRepository.findAll();
        assertThat(balanceDetailsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBalanceDetails() throws Exception {
        // Initialize the database
        balanceDetailsRepository.saveAndFlush(balanceDetails);
        int databaseSizeBeforeDelete = balanceDetailsRepository.findAll().size();

        // Get the balanceDetails
        restBalanceDetailsMockMvc.perform(delete("/api/balance-details/{id}", balanceDetails.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<BalanceDetails> balanceDetailsList = balanceDetailsRepository.findAll();
        assertThat(balanceDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BalanceDetails.class);
        BalanceDetails balanceDetails1 = new BalanceDetails();
        balanceDetails1.setId(1L);
        BalanceDetails balanceDetails2 = new BalanceDetails();
        balanceDetails2.setId(balanceDetails1.getId());
        assertThat(balanceDetails1).isEqualTo(balanceDetails2);
        balanceDetails2.setId(2L);
        assertThat(balanceDetails1).isNotEqualTo(balanceDetails2);
        balanceDetails1.setId(null);
        assertThat(balanceDetails1).isNotEqualTo(balanceDetails2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BalanceDetailsDTO.class);
        BalanceDetailsDTO balanceDetailsDTO1 = new BalanceDetailsDTO();
        balanceDetailsDTO1.setId(1L);
        BalanceDetailsDTO balanceDetailsDTO2 = new BalanceDetailsDTO();
        assertThat(balanceDetailsDTO1).isNotEqualTo(balanceDetailsDTO2);
        balanceDetailsDTO2.setId(balanceDetailsDTO1.getId());
        assertThat(balanceDetailsDTO1).isEqualTo(balanceDetailsDTO2);
        balanceDetailsDTO2.setId(2L);
        assertThat(balanceDetailsDTO1).isNotEqualTo(balanceDetailsDTO2);
        balanceDetailsDTO1.setId(null);
        assertThat(balanceDetailsDTO1).isNotEqualTo(balanceDetailsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(balanceDetailsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(balanceDetailsMapper.fromId(null)).isNull();
    }
}
