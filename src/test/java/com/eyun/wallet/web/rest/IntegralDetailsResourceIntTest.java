package com.eyun.wallet.web.rest;

import com.eyun.wallet.WalletApp;

import com.eyun.wallet.config.SecurityBeanOverrideConfiguration;

import com.eyun.wallet.domain.IntegralDetails;
import com.eyun.wallet.domain.Wallet;
import com.eyun.wallet.repository.IntegralDetailsRepository;
import com.eyun.wallet.service.IntegralDetailsService;
import com.eyun.wallet.service.dto.IntegralDetailsDTO;
import com.eyun.wallet.service.mapper.IntegralDetailsMapper;
import com.eyun.wallet.web.rest.errors.ExceptionTranslator;
import com.eyun.wallet.service.dto.IntegralDetailsCriteria;
import com.eyun.wallet.service.IntegralDetailsQueryService;

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
 * Test class for the IntegralDetailsResource REST controller.
 *
 * @see IntegralDetailsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WalletApp.class, SecurityBeanOverrideConfiguration.class})
public class IntegralDetailsResourceIntTest {

    private static final Long DEFAULT_USERID = 1L;
    private static final Long UPDATED_USERID = 2L;

    private static final BigDecimal DEFAULT_INTEGRAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_INTEGRAL = new BigDecimal(2);

    private static final Boolean DEFAULT_ADD_INTEGRAL = false;
    private static final Boolean UPDATED_ADD_INTEGRAL = true;

    private static final Integer DEFAULT_TYPE = 1;
    private static final Integer UPDATED_TYPE = 2;

    private static final String DEFAULT_TYPE_STRING = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_STRING = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ORDER_NO = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_NO = "BBBBBBBBBB";

    @Autowired
    private IntegralDetailsRepository integralDetailsRepository;

    @Autowired
    private IntegralDetailsMapper integralDetailsMapper;

    @Autowired
    private IntegralDetailsService integralDetailsService;

    @Autowired
    private IntegralDetailsQueryService integralDetailsQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restIntegralDetailsMockMvc;

    private IntegralDetails integralDetails;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IntegralDetailsResource integralDetailsResource = new IntegralDetailsResource(integralDetailsService, integralDetailsQueryService);
        this.restIntegralDetailsMockMvc = MockMvcBuilders.standaloneSetup(integralDetailsResource)
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
    public static IntegralDetails createEntity(EntityManager em) {
        IntegralDetails integralDetails = new IntegralDetails()
            .userid(DEFAULT_USERID)
            .integral(DEFAULT_INTEGRAL)
            .addIntegral(DEFAULT_ADD_INTEGRAL)
            .type(DEFAULT_TYPE)
            .typeString(DEFAULT_TYPE_STRING)
            .createdTime(DEFAULT_CREATED_TIME)
            .orderNo(DEFAULT_ORDER_NO);
        return integralDetails;
    }

    @Before
    public void initTest() {
        integralDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createIntegralDetails() throws Exception {
        int databaseSizeBeforeCreate = integralDetailsRepository.findAll().size();

        // Create the IntegralDetails
        IntegralDetailsDTO integralDetailsDTO = integralDetailsMapper.toDto(integralDetails);
        restIntegralDetailsMockMvc.perform(post("/api/integral-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(integralDetailsDTO)))
            .andExpect(status().isCreated());

        // Validate the IntegralDetails in the database
        List<IntegralDetails> integralDetailsList = integralDetailsRepository.findAll();
        assertThat(integralDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        IntegralDetails testIntegralDetails = integralDetailsList.get(integralDetailsList.size() - 1);
        assertThat(testIntegralDetails.getUserid()).isEqualTo(DEFAULT_USERID);
        assertThat(testIntegralDetails.getIntegral()).isEqualTo(DEFAULT_INTEGRAL);
        assertThat(testIntegralDetails.isAddIntegral()).isEqualTo(DEFAULT_ADD_INTEGRAL);
        assertThat(testIntegralDetails.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testIntegralDetails.getTypeString()).isEqualTo(DEFAULT_TYPE_STRING);
        assertThat(testIntegralDetails.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testIntegralDetails.getOrderNo()).isEqualTo(DEFAULT_ORDER_NO);
    }

    @Test
    @Transactional
    public void createIntegralDetailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = integralDetailsRepository.findAll().size();

        // Create the IntegralDetails with an existing ID
        integralDetails.setId(1L);
        IntegralDetailsDTO integralDetailsDTO = integralDetailsMapper.toDto(integralDetails);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIntegralDetailsMockMvc.perform(post("/api/integral-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(integralDetailsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the IntegralDetails in the database
        List<IntegralDetails> integralDetailsList = integralDetailsRepository.findAll();
        assertThat(integralDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllIntegralDetails() throws Exception {
        // Initialize the database
        integralDetailsRepository.saveAndFlush(integralDetails);

        // Get all the integralDetailsList
        restIntegralDetailsMockMvc.perform(get("/api/integral-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(integralDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].userid").value(hasItem(DEFAULT_USERID.intValue())))
            .andExpect(jsonPath("$.[*].integral").value(hasItem(DEFAULT_INTEGRAL.intValue())))
            .andExpect(jsonPath("$.[*].addIntegral").value(hasItem(DEFAULT_ADD_INTEGRAL.booleanValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].typeString").value(hasItem(DEFAULT_TYPE_STRING.toString())))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(DEFAULT_CREATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].orderNo").value(hasItem(DEFAULT_ORDER_NO.toString())));
    }

    @Test
    @Transactional
    public void getIntegralDetails() throws Exception {
        // Initialize the database
        integralDetailsRepository.saveAndFlush(integralDetails);

        // Get the integralDetails
        restIntegralDetailsMockMvc.perform(get("/api/integral-details/{id}", integralDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(integralDetails.getId().intValue()))
            .andExpect(jsonPath("$.userid").value(DEFAULT_USERID.intValue()))
            .andExpect(jsonPath("$.integral").value(DEFAULT_INTEGRAL.intValue()))
            .andExpect(jsonPath("$.addIntegral").value(DEFAULT_ADD_INTEGRAL.booleanValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.typeString").value(DEFAULT_TYPE_STRING.toString()))
            .andExpect(jsonPath("$.createdTime").value(DEFAULT_CREATED_TIME.toString()))
            .andExpect(jsonPath("$.orderNo").value(DEFAULT_ORDER_NO.toString()));
    }

    @Test
    @Transactional
    public void getAllIntegralDetailsByUseridIsEqualToSomething() throws Exception {
        // Initialize the database
        integralDetailsRepository.saveAndFlush(integralDetails);

        // Get all the integralDetailsList where userid equals to DEFAULT_USERID
        defaultIntegralDetailsShouldBeFound("userid.equals=" + DEFAULT_USERID);

        // Get all the integralDetailsList where userid equals to UPDATED_USERID
        defaultIntegralDetailsShouldNotBeFound("userid.equals=" + UPDATED_USERID);
    }

    @Test
    @Transactional
    public void getAllIntegralDetailsByUseridIsInShouldWork() throws Exception {
        // Initialize the database
        integralDetailsRepository.saveAndFlush(integralDetails);

        // Get all the integralDetailsList where userid in DEFAULT_USERID or UPDATED_USERID
        defaultIntegralDetailsShouldBeFound("userid.in=" + DEFAULT_USERID + "," + UPDATED_USERID);

        // Get all the integralDetailsList where userid equals to UPDATED_USERID
        defaultIntegralDetailsShouldNotBeFound("userid.in=" + UPDATED_USERID);
    }

    @Test
    @Transactional
    public void getAllIntegralDetailsByUseridIsNullOrNotNull() throws Exception {
        // Initialize the database
        integralDetailsRepository.saveAndFlush(integralDetails);

        // Get all the integralDetailsList where userid is not null
        defaultIntegralDetailsShouldBeFound("userid.specified=true");

        // Get all the integralDetailsList where userid is null
        defaultIntegralDetailsShouldNotBeFound("userid.specified=false");
    }

    @Test
    @Transactional
    public void getAllIntegralDetailsByUseridIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        integralDetailsRepository.saveAndFlush(integralDetails);

        // Get all the integralDetailsList where userid greater than or equals to DEFAULT_USERID
        defaultIntegralDetailsShouldBeFound("userid.greaterOrEqualThan=" + DEFAULT_USERID);

        // Get all the integralDetailsList where userid greater than or equals to UPDATED_USERID
        defaultIntegralDetailsShouldNotBeFound("userid.greaterOrEqualThan=" + UPDATED_USERID);
    }

    @Test
    @Transactional
    public void getAllIntegralDetailsByUseridIsLessThanSomething() throws Exception {
        // Initialize the database
        integralDetailsRepository.saveAndFlush(integralDetails);

        // Get all the integralDetailsList where userid less than or equals to DEFAULT_USERID
        defaultIntegralDetailsShouldNotBeFound("userid.lessThan=" + DEFAULT_USERID);

        // Get all the integralDetailsList where userid less than or equals to UPDATED_USERID
        defaultIntegralDetailsShouldBeFound("userid.lessThan=" + UPDATED_USERID);
    }


    @Test
    @Transactional
    public void getAllIntegralDetailsByIntegralIsEqualToSomething() throws Exception {
        // Initialize the database
        integralDetailsRepository.saveAndFlush(integralDetails);

        // Get all the integralDetailsList where integral equals to DEFAULT_INTEGRAL
        defaultIntegralDetailsShouldBeFound("integral.equals=" + DEFAULT_INTEGRAL);

        // Get all the integralDetailsList where integral equals to UPDATED_INTEGRAL
        defaultIntegralDetailsShouldNotBeFound("integral.equals=" + UPDATED_INTEGRAL);
    }

    @Test
    @Transactional
    public void getAllIntegralDetailsByIntegralIsInShouldWork() throws Exception {
        // Initialize the database
        integralDetailsRepository.saveAndFlush(integralDetails);

        // Get all the integralDetailsList where integral in DEFAULT_INTEGRAL or UPDATED_INTEGRAL
        defaultIntegralDetailsShouldBeFound("integral.in=" + DEFAULT_INTEGRAL + "," + UPDATED_INTEGRAL);

        // Get all the integralDetailsList where integral equals to UPDATED_INTEGRAL
        defaultIntegralDetailsShouldNotBeFound("integral.in=" + UPDATED_INTEGRAL);
    }

    @Test
    @Transactional
    public void getAllIntegralDetailsByIntegralIsNullOrNotNull() throws Exception {
        // Initialize the database
        integralDetailsRepository.saveAndFlush(integralDetails);

        // Get all the integralDetailsList where integral is not null
        defaultIntegralDetailsShouldBeFound("integral.specified=true");

        // Get all the integralDetailsList where integral is null
        defaultIntegralDetailsShouldNotBeFound("integral.specified=false");
    }

    @Test
    @Transactional
    public void getAllIntegralDetailsByAddIntegralIsEqualToSomething() throws Exception {
        // Initialize the database
        integralDetailsRepository.saveAndFlush(integralDetails);

        // Get all the integralDetailsList where addIntegral equals to DEFAULT_ADD_INTEGRAL
        defaultIntegralDetailsShouldBeFound("addIntegral.equals=" + DEFAULT_ADD_INTEGRAL);

        // Get all the integralDetailsList where addIntegral equals to UPDATED_ADD_INTEGRAL
        defaultIntegralDetailsShouldNotBeFound("addIntegral.equals=" + UPDATED_ADD_INTEGRAL);
    }

    @Test
    @Transactional
    public void getAllIntegralDetailsByAddIntegralIsInShouldWork() throws Exception {
        // Initialize the database
        integralDetailsRepository.saveAndFlush(integralDetails);

        // Get all the integralDetailsList where addIntegral in DEFAULT_ADD_INTEGRAL or UPDATED_ADD_INTEGRAL
        defaultIntegralDetailsShouldBeFound("addIntegral.in=" + DEFAULT_ADD_INTEGRAL + "," + UPDATED_ADD_INTEGRAL);

        // Get all the integralDetailsList where addIntegral equals to UPDATED_ADD_INTEGRAL
        defaultIntegralDetailsShouldNotBeFound("addIntegral.in=" + UPDATED_ADD_INTEGRAL);
    }

    @Test
    @Transactional
    public void getAllIntegralDetailsByAddIntegralIsNullOrNotNull() throws Exception {
        // Initialize the database
        integralDetailsRepository.saveAndFlush(integralDetails);

        // Get all the integralDetailsList where addIntegral is not null
        defaultIntegralDetailsShouldBeFound("addIntegral.specified=true");

        // Get all the integralDetailsList where addIntegral is null
        defaultIntegralDetailsShouldNotBeFound("addIntegral.specified=false");
    }

    @Test
    @Transactional
    public void getAllIntegralDetailsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        integralDetailsRepository.saveAndFlush(integralDetails);

        // Get all the integralDetailsList where type equals to DEFAULT_TYPE
        defaultIntegralDetailsShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the integralDetailsList where type equals to UPDATED_TYPE
        defaultIntegralDetailsShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllIntegralDetailsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        integralDetailsRepository.saveAndFlush(integralDetails);

        // Get all the integralDetailsList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultIntegralDetailsShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the integralDetailsList where type equals to UPDATED_TYPE
        defaultIntegralDetailsShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllIntegralDetailsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        integralDetailsRepository.saveAndFlush(integralDetails);

        // Get all the integralDetailsList where type is not null
        defaultIntegralDetailsShouldBeFound("type.specified=true");

        // Get all the integralDetailsList where type is null
        defaultIntegralDetailsShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllIntegralDetailsByTypeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        integralDetailsRepository.saveAndFlush(integralDetails);

        // Get all the integralDetailsList where type greater than or equals to DEFAULT_TYPE
        defaultIntegralDetailsShouldBeFound("type.greaterOrEqualThan=" + DEFAULT_TYPE);

        // Get all the integralDetailsList where type greater than or equals to UPDATED_TYPE
        defaultIntegralDetailsShouldNotBeFound("type.greaterOrEqualThan=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllIntegralDetailsByTypeIsLessThanSomething() throws Exception {
        // Initialize the database
        integralDetailsRepository.saveAndFlush(integralDetails);

        // Get all the integralDetailsList where type less than or equals to DEFAULT_TYPE
        defaultIntegralDetailsShouldNotBeFound("type.lessThan=" + DEFAULT_TYPE);

        // Get all the integralDetailsList where type less than or equals to UPDATED_TYPE
        defaultIntegralDetailsShouldBeFound("type.lessThan=" + UPDATED_TYPE);
    }


    @Test
    @Transactional
    public void getAllIntegralDetailsByTypeStringIsEqualToSomething() throws Exception {
        // Initialize the database
        integralDetailsRepository.saveAndFlush(integralDetails);

        // Get all the integralDetailsList where typeString equals to DEFAULT_TYPE_STRING
        defaultIntegralDetailsShouldBeFound("typeString.equals=" + DEFAULT_TYPE_STRING);

        // Get all the integralDetailsList where typeString equals to UPDATED_TYPE_STRING
        defaultIntegralDetailsShouldNotBeFound("typeString.equals=" + UPDATED_TYPE_STRING);
    }

    @Test
    @Transactional
    public void getAllIntegralDetailsByTypeStringIsInShouldWork() throws Exception {
        // Initialize the database
        integralDetailsRepository.saveAndFlush(integralDetails);

        // Get all the integralDetailsList where typeString in DEFAULT_TYPE_STRING or UPDATED_TYPE_STRING
        defaultIntegralDetailsShouldBeFound("typeString.in=" + DEFAULT_TYPE_STRING + "," + UPDATED_TYPE_STRING);

        // Get all the integralDetailsList where typeString equals to UPDATED_TYPE_STRING
        defaultIntegralDetailsShouldNotBeFound("typeString.in=" + UPDATED_TYPE_STRING);
    }

    @Test
    @Transactional
    public void getAllIntegralDetailsByTypeStringIsNullOrNotNull() throws Exception {
        // Initialize the database
        integralDetailsRepository.saveAndFlush(integralDetails);

        // Get all the integralDetailsList where typeString is not null
        defaultIntegralDetailsShouldBeFound("typeString.specified=true");

        // Get all the integralDetailsList where typeString is null
        defaultIntegralDetailsShouldNotBeFound("typeString.specified=false");
    }

    @Test
    @Transactional
    public void getAllIntegralDetailsByCreatedTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        integralDetailsRepository.saveAndFlush(integralDetails);

        // Get all the integralDetailsList where createdTime equals to DEFAULT_CREATED_TIME
        defaultIntegralDetailsShouldBeFound("createdTime.equals=" + DEFAULT_CREATED_TIME);

        // Get all the integralDetailsList where createdTime equals to UPDATED_CREATED_TIME
        defaultIntegralDetailsShouldNotBeFound("createdTime.equals=" + UPDATED_CREATED_TIME);
    }

    @Test
    @Transactional
    public void getAllIntegralDetailsByCreatedTimeIsInShouldWork() throws Exception {
        // Initialize the database
        integralDetailsRepository.saveAndFlush(integralDetails);

        // Get all the integralDetailsList where createdTime in DEFAULT_CREATED_TIME or UPDATED_CREATED_TIME
        defaultIntegralDetailsShouldBeFound("createdTime.in=" + DEFAULT_CREATED_TIME + "," + UPDATED_CREATED_TIME);

        // Get all the integralDetailsList where createdTime equals to UPDATED_CREATED_TIME
        defaultIntegralDetailsShouldNotBeFound("createdTime.in=" + UPDATED_CREATED_TIME);
    }

    @Test
    @Transactional
    public void getAllIntegralDetailsByCreatedTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        integralDetailsRepository.saveAndFlush(integralDetails);

        // Get all the integralDetailsList where createdTime is not null
        defaultIntegralDetailsShouldBeFound("createdTime.specified=true");

        // Get all the integralDetailsList where createdTime is null
        defaultIntegralDetailsShouldNotBeFound("createdTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllIntegralDetailsByOrderNoIsEqualToSomething() throws Exception {
        // Initialize the database
        integralDetailsRepository.saveAndFlush(integralDetails);

        // Get all the integralDetailsList where orderNo equals to DEFAULT_ORDER_NO
        defaultIntegralDetailsShouldBeFound("orderNo.equals=" + DEFAULT_ORDER_NO);

        // Get all the integralDetailsList where orderNo equals to UPDATED_ORDER_NO
        defaultIntegralDetailsShouldNotBeFound("orderNo.equals=" + UPDATED_ORDER_NO);
    }

    @Test
    @Transactional
    public void getAllIntegralDetailsByOrderNoIsInShouldWork() throws Exception {
        // Initialize the database
        integralDetailsRepository.saveAndFlush(integralDetails);

        // Get all the integralDetailsList where orderNo in DEFAULT_ORDER_NO or UPDATED_ORDER_NO
        defaultIntegralDetailsShouldBeFound("orderNo.in=" + DEFAULT_ORDER_NO + "," + UPDATED_ORDER_NO);

        // Get all the integralDetailsList where orderNo equals to UPDATED_ORDER_NO
        defaultIntegralDetailsShouldNotBeFound("orderNo.in=" + UPDATED_ORDER_NO);
    }

    @Test
    @Transactional
    public void getAllIntegralDetailsByOrderNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        integralDetailsRepository.saveAndFlush(integralDetails);

        // Get all the integralDetailsList where orderNo is not null
        defaultIntegralDetailsShouldBeFound("orderNo.specified=true");

        // Get all the integralDetailsList where orderNo is null
        defaultIntegralDetailsShouldNotBeFound("orderNo.specified=false");
    }

    @Test
    @Transactional
    public void getAllIntegralDetailsByWalletIsEqualToSomething() throws Exception {
        // Initialize the database
        Wallet wallet = WalletResourceIntTest.createEntity(em);
        em.persist(wallet);
        em.flush();
        integralDetails.setWallet(wallet);
        integralDetailsRepository.saveAndFlush(integralDetails);
        Long walletId = wallet.getId();

        // Get all the integralDetailsList where wallet equals to walletId
        defaultIntegralDetailsShouldBeFound("walletId.equals=" + walletId);

        // Get all the integralDetailsList where wallet equals to walletId + 1
        defaultIntegralDetailsShouldNotBeFound("walletId.equals=" + (walletId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultIntegralDetailsShouldBeFound(String filter) throws Exception {
        restIntegralDetailsMockMvc.perform(get("/api/integral-details?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(integralDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].userid").value(hasItem(DEFAULT_USERID.intValue())))
            .andExpect(jsonPath("$.[*].integral").value(hasItem(DEFAULT_INTEGRAL.intValue())))
            .andExpect(jsonPath("$.[*].addIntegral").value(hasItem(DEFAULT_ADD_INTEGRAL.booleanValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].typeString").value(hasItem(DEFAULT_TYPE_STRING.toString())))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(DEFAULT_CREATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].orderNo").value(hasItem(DEFAULT_ORDER_NO.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultIntegralDetailsShouldNotBeFound(String filter) throws Exception {
        restIntegralDetailsMockMvc.perform(get("/api/integral-details?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingIntegralDetails() throws Exception {
        // Get the integralDetails
        restIntegralDetailsMockMvc.perform(get("/api/integral-details/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIntegralDetails() throws Exception {
        // Initialize the database
        integralDetailsRepository.saveAndFlush(integralDetails);
        int databaseSizeBeforeUpdate = integralDetailsRepository.findAll().size();

        // Update the integralDetails
        IntegralDetails updatedIntegralDetails = integralDetailsRepository.findOne(integralDetails.getId());
        // Disconnect from session so that the updates on updatedIntegralDetails are not directly saved in db
        em.detach(updatedIntegralDetails);
        updatedIntegralDetails
            .userid(UPDATED_USERID)
            .integral(UPDATED_INTEGRAL)
            .addIntegral(UPDATED_ADD_INTEGRAL)
            .type(UPDATED_TYPE)
            .typeString(UPDATED_TYPE_STRING)
            .createdTime(UPDATED_CREATED_TIME)
            .orderNo(UPDATED_ORDER_NO);
        IntegralDetailsDTO integralDetailsDTO = integralDetailsMapper.toDto(updatedIntegralDetails);

        restIntegralDetailsMockMvc.perform(put("/api/integral-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(integralDetailsDTO)))
            .andExpect(status().isOk());

        // Validate the IntegralDetails in the database
        List<IntegralDetails> integralDetailsList = integralDetailsRepository.findAll();
        assertThat(integralDetailsList).hasSize(databaseSizeBeforeUpdate);
        IntegralDetails testIntegralDetails = integralDetailsList.get(integralDetailsList.size() - 1);
        assertThat(testIntegralDetails.getUserid()).isEqualTo(UPDATED_USERID);
        assertThat(testIntegralDetails.getIntegral()).isEqualTo(UPDATED_INTEGRAL);
        assertThat(testIntegralDetails.isAddIntegral()).isEqualTo(UPDATED_ADD_INTEGRAL);
        assertThat(testIntegralDetails.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testIntegralDetails.getTypeString()).isEqualTo(UPDATED_TYPE_STRING);
        assertThat(testIntegralDetails.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testIntegralDetails.getOrderNo()).isEqualTo(UPDATED_ORDER_NO);
    }

    @Test
    @Transactional
    public void updateNonExistingIntegralDetails() throws Exception {
        int databaseSizeBeforeUpdate = integralDetailsRepository.findAll().size();

        // Create the IntegralDetails
        IntegralDetailsDTO integralDetailsDTO = integralDetailsMapper.toDto(integralDetails);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restIntegralDetailsMockMvc.perform(put("/api/integral-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(integralDetailsDTO)))
            .andExpect(status().isCreated());

        // Validate the IntegralDetails in the database
        List<IntegralDetails> integralDetailsList = integralDetailsRepository.findAll();
        assertThat(integralDetailsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteIntegralDetails() throws Exception {
        // Initialize the database
        integralDetailsRepository.saveAndFlush(integralDetails);
        int databaseSizeBeforeDelete = integralDetailsRepository.findAll().size();

        // Get the integralDetails
        restIntegralDetailsMockMvc.perform(delete("/api/integral-details/{id}", integralDetails.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<IntegralDetails> integralDetailsList = integralDetailsRepository.findAll();
        assertThat(integralDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IntegralDetails.class);
        IntegralDetails integralDetails1 = new IntegralDetails();
        integralDetails1.setId(1L);
        IntegralDetails integralDetails2 = new IntegralDetails();
        integralDetails2.setId(integralDetails1.getId());
        assertThat(integralDetails1).isEqualTo(integralDetails2);
        integralDetails2.setId(2L);
        assertThat(integralDetails1).isNotEqualTo(integralDetails2);
        integralDetails1.setId(null);
        assertThat(integralDetails1).isNotEqualTo(integralDetails2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IntegralDetailsDTO.class);
        IntegralDetailsDTO integralDetailsDTO1 = new IntegralDetailsDTO();
        integralDetailsDTO1.setId(1L);
        IntegralDetailsDTO integralDetailsDTO2 = new IntegralDetailsDTO();
        assertThat(integralDetailsDTO1).isNotEqualTo(integralDetailsDTO2);
        integralDetailsDTO2.setId(integralDetailsDTO1.getId());
        assertThat(integralDetailsDTO1).isEqualTo(integralDetailsDTO2);
        integralDetailsDTO2.setId(2L);
        assertThat(integralDetailsDTO1).isNotEqualTo(integralDetailsDTO2);
        integralDetailsDTO1.setId(null);
        assertThat(integralDetailsDTO1).isNotEqualTo(integralDetailsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(integralDetailsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(integralDetailsMapper.fromId(null)).isNull();
    }
}
