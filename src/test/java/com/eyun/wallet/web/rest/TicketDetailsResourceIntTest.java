package com.eyun.wallet.web.rest;

import com.eyun.wallet.WalletApp;

import com.eyun.wallet.config.SecurityBeanOverrideConfiguration;

import com.eyun.wallet.domain.TicketDetails;
import com.eyun.wallet.domain.Wallet;
import com.eyun.wallet.repository.TicketDetailsRepository;
import com.eyun.wallet.service.TicketDetailsService;
import com.eyun.wallet.service.dto.TicketDetailsDTO;
import com.eyun.wallet.service.mapper.TicketDetailsMapper;
import com.eyun.wallet.web.rest.errors.ExceptionTranslator;
import com.eyun.wallet.service.dto.TicketDetailsCriteria;
import com.eyun.wallet.service.TicketDetailsQueryService;

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
 * Test class for the TicketDetailsResource REST controller.
 *
 * @see TicketDetailsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WalletApp.class, SecurityBeanOverrideConfiguration.class})
public class TicketDetailsResourceIntTest {

    private static final Long DEFAULT_USERID = 1L;
    private static final Long UPDATED_USERID = 2L;

    private static final Long DEFAULT_AMOUNT = 1L;
    private static final Long UPDATED_AMOUNT = 2L;

    private static final Boolean DEFAULT_ADD_AMOUNT = false;
    private static final Boolean UPDATED_ADD_AMOUNT = true;

    private static final Integer DEFAULT_TYPE = 1;
    private static final Integer UPDATED_TYPE = 2;

    private static final String DEFAULT_TYPE_STRING = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_STRING = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_TICKET = new BigDecimal(1);
    private static final BigDecimal UPDATED_TICKET = new BigDecimal(2);

    private static final String DEFAULT_ORDER_NO = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_NO = "BBBBBBBBBB";

    @Autowired
    private TicketDetailsRepository ticketDetailsRepository;

    @Autowired
    private TicketDetailsMapper ticketDetailsMapper;

    @Autowired
    private TicketDetailsService ticketDetailsService;

    @Autowired
    private TicketDetailsQueryService ticketDetailsQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTicketDetailsMockMvc;

    private TicketDetails ticketDetails;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TicketDetailsResource ticketDetailsResource = new TicketDetailsResource(ticketDetailsService, ticketDetailsQueryService);
        this.restTicketDetailsMockMvc = MockMvcBuilders.standaloneSetup(ticketDetailsResource)
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
    public static TicketDetails createEntity(EntityManager em) {
        TicketDetails ticketDetails = new TicketDetails()
            .userid(DEFAULT_USERID)
            .amount(DEFAULT_AMOUNT)
            .addAmount(DEFAULT_ADD_AMOUNT)
            .type(DEFAULT_TYPE)
            .typeString(DEFAULT_TYPE_STRING)
            .createdTime(DEFAULT_CREATED_TIME)
            .ticket(DEFAULT_TICKET)
            .orderNo(DEFAULT_ORDER_NO);
        return ticketDetails;
    }

    @Before
    public void initTest() {
        ticketDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createTicketDetails() throws Exception {
        int databaseSizeBeforeCreate = ticketDetailsRepository.findAll().size();

        // Create the TicketDetails
        TicketDetailsDTO ticketDetailsDTO = ticketDetailsMapper.toDto(ticketDetails);
        restTicketDetailsMockMvc.perform(post("/api/ticket-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ticketDetailsDTO)))
            .andExpect(status().isCreated());

        // Validate the TicketDetails in the database
        List<TicketDetails> ticketDetailsList = ticketDetailsRepository.findAll();
        assertThat(ticketDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        TicketDetails testTicketDetails = ticketDetailsList.get(ticketDetailsList.size() - 1);
        assertThat(testTicketDetails.getUserid()).isEqualTo(DEFAULT_USERID);
        assertThat(testTicketDetails.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testTicketDetails.isAddAmount()).isEqualTo(DEFAULT_ADD_AMOUNT);
        assertThat(testTicketDetails.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testTicketDetails.getTypeString()).isEqualTo(DEFAULT_TYPE_STRING);
        assertThat(testTicketDetails.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testTicketDetails.getTicket()).isEqualTo(DEFAULT_TICKET);
        assertThat(testTicketDetails.getOrderNo()).isEqualTo(DEFAULT_ORDER_NO);
    }

    @Test
    @Transactional
    public void createTicketDetailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ticketDetailsRepository.findAll().size();

        // Create the TicketDetails with an existing ID
        ticketDetails.setId(1L);
        TicketDetailsDTO ticketDetailsDTO = ticketDetailsMapper.toDto(ticketDetails);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTicketDetailsMockMvc.perform(post("/api/ticket-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ticketDetailsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TicketDetails in the database
        List<TicketDetails> ticketDetailsList = ticketDetailsRepository.findAll();
        assertThat(ticketDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTicketDetails() throws Exception {
        // Initialize the database
        ticketDetailsRepository.saveAndFlush(ticketDetails);

        // Get all the ticketDetailsList
        restTicketDetailsMockMvc.perform(get("/api/ticket-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ticketDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].userid").value(hasItem(DEFAULT_USERID.intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].addAmount").value(hasItem(DEFAULT_ADD_AMOUNT.booleanValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].typeString").value(hasItem(DEFAULT_TYPE_STRING.toString())))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(DEFAULT_CREATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].ticket").value(hasItem(DEFAULT_TICKET.intValue())))
            .andExpect(jsonPath("$.[*].orderNo").value(hasItem(DEFAULT_ORDER_NO.toString())));
    }

    @Test
    @Transactional
    public void getTicketDetails() throws Exception {
        // Initialize the database
        ticketDetailsRepository.saveAndFlush(ticketDetails);

        // Get the ticketDetails
        restTicketDetailsMockMvc.perform(get("/api/ticket-details/{id}", ticketDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ticketDetails.getId().intValue()))
            .andExpect(jsonPath("$.userid").value(DEFAULT_USERID.intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.addAmount").value(DEFAULT_ADD_AMOUNT.booleanValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.typeString").value(DEFAULT_TYPE_STRING.toString()))
            .andExpect(jsonPath("$.createdTime").value(DEFAULT_CREATED_TIME.toString()))
            .andExpect(jsonPath("$.ticket").value(DEFAULT_TICKET.intValue()))
            .andExpect(jsonPath("$.orderNo").value(DEFAULT_ORDER_NO.toString()));
    }

    @Test
    @Transactional
    public void getAllTicketDetailsByUseridIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketDetailsRepository.saveAndFlush(ticketDetails);

        // Get all the ticketDetailsList where userid equals to DEFAULT_USERID
        defaultTicketDetailsShouldBeFound("userid.equals=" + DEFAULT_USERID);

        // Get all the ticketDetailsList where userid equals to UPDATED_USERID
        defaultTicketDetailsShouldNotBeFound("userid.equals=" + UPDATED_USERID);
    }

    @Test
    @Transactional
    public void getAllTicketDetailsByUseridIsInShouldWork() throws Exception {
        // Initialize the database
        ticketDetailsRepository.saveAndFlush(ticketDetails);

        // Get all the ticketDetailsList where userid in DEFAULT_USERID or UPDATED_USERID
        defaultTicketDetailsShouldBeFound("userid.in=" + DEFAULT_USERID + "," + UPDATED_USERID);

        // Get all the ticketDetailsList where userid equals to UPDATED_USERID
        defaultTicketDetailsShouldNotBeFound("userid.in=" + UPDATED_USERID);
    }

    @Test
    @Transactional
    public void getAllTicketDetailsByUseridIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketDetailsRepository.saveAndFlush(ticketDetails);

        // Get all the ticketDetailsList where userid is not null
        defaultTicketDetailsShouldBeFound("userid.specified=true");

        // Get all the ticketDetailsList where userid is null
        defaultTicketDetailsShouldNotBeFound("userid.specified=false");
    }

    @Test
    @Transactional
    public void getAllTicketDetailsByUseridIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ticketDetailsRepository.saveAndFlush(ticketDetails);

        // Get all the ticketDetailsList where userid greater than or equals to DEFAULT_USERID
        defaultTicketDetailsShouldBeFound("userid.greaterOrEqualThan=" + DEFAULT_USERID);

        // Get all the ticketDetailsList where userid greater than or equals to UPDATED_USERID
        defaultTicketDetailsShouldNotBeFound("userid.greaterOrEqualThan=" + UPDATED_USERID);
    }

    @Test
    @Transactional
    public void getAllTicketDetailsByUseridIsLessThanSomething() throws Exception {
        // Initialize the database
        ticketDetailsRepository.saveAndFlush(ticketDetails);

        // Get all the ticketDetailsList where userid less than or equals to DEFAULT_USERID
        defaultTicketDetailsShouldNotBeFound("userid.lessThan=" + DEFAULT_USERID);

        // Get all the ticketDetailsList where userid less than or equals to UPDATED_USERID
        defaultTicketDetailsShouldBeFound("userid.lessThan=" + UPDATED_USERID);
    }


    @Test
    @Transactional
    public void getAllTicketDetailsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketDetailsRepository.saveAndFlush(ticketDetails);

        // Get all the ticketDetailsList where amount equals to DEFAULT_AMOUNT
        defaultTicketDetailsShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the ticketDetailsList where amount equals to UPDATED_AMOUNT
        defaultTicketDetailsShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllTicketDetailsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        ticketDetailsRepository.saveAndFlush(ticketDetails);

        // Get all the ticketDetailsList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultTicketDetailsShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the ticketDetailsList where amount equals to UPDATED_AMOUNT
        defaultTicketDetailsShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllTicketDetailsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketDetailsRepository.saveAndFlush(ticketDetails);

        // Get all the ticketDetailsList where amount is not null
        defaultTicketDetailsShouldBeFound("amount.specified=true");

        // Get all the ticketDetailsList where amount is null
        defaultTicketDetailsShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    public void getAllTicketDetailsByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ticketDetailsRepository.saveAndFlush(ticketDetails);

        // Get all the ticketDetailsList where amount greater than or equals to DEFAULT_AMOUNT
        defaultTicketDetailsShouldBeFound("amount.greaterOrEqualThan=" + DEFAULT_AMOUNT);

        // Get all the ticketDetailsList where amount greater than or equals to UPDATED_AMOUNT
        defaultTicketDetailsShouldNotBeFound("amount.greaterOrEqualThan=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllTicketDetailsByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        ticketDetailsRepository.saveAndFlush(ticketDetails);

        // Get all the ticketDetailsList where amount less than or equals to DEFAULT_AMOUNT
        defaultTicketDetailsShouldNotBeFound("amount.lessThan=" + DEFAULT_AMOUNT);

        // Get all the ticketDetailsList where amount less than or equals to UPDATED_AMOUNT
        defaultTicketDetailsShouldBeFound("amount.lessThan=" + UPDATED_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllTicketDetailsByAddAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketDetailsRepository.saveAndFlush(ticketDetails);

        // Get all the ticketDetailsList where addAmount equals to DEFAULT_ADD_AMOUNT
        defaultTicketDetailsShouldBeFound("addAmount.equals=" + DEFAULT_ADD_AMOUNT);

        // Get all the ticketDetailsList where addAmount equals to UPDATED_ADD_AMOUNT
        defaultTicketDetailsShouldNotBeFound("addAmount.equals=" + UPDATED_ADD_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllTicketDetailsByAddAmountIsInShouldWork() throws Exception {
        // Initialize the database
        ticketDetailsRepository.saveAndFlush(ticketDetails);

        // Get all the ticketDetailsList where addAmount in DEFAULT_ADD_AMOUNT or UPDATED_ADD_AMOUNT
        defaultTicketDetailsShouldBeFound("addAmount.in=" + DEFAULT_ADD_AMOUNT + "," + UPDATED_ADD_AMOUNT);

        // Get all the ticketDetailsList where addAmount equals to UPDATED_ADD_AMOUNT
        defaultTicketDetailsShouldNotBeFound("addAmount.in=" + UPDATED_ADD_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllTicketDetailsByAddAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketDetailsRepository.saveAndFlush(ticketDetails);

        // Get all the ticketDetailsList where addAmount is not null
        defaultTicketDetailsShouldBeFound("addAmount.specified=true");

        // Get all the ticketDetailsList where addAmount is null
        defaultTicketDetailsShouldNotBeFound("addAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllTicketDetailsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketDetailsRepository.saveAndFlush(ticketDetails);

        // Get all the ticketDetailsList where type equals to DEFAULT_TYPE
        defaultTicketDetailsShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the ticketDetailsList where type equals to UPDATED_TYPE
        defaultTicketDetailsShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllTicketDetailsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        ticketDetailsRepository.saveAndFlush(ticketDetails);

        // Get all the ticketDetailsList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultTicketDetailsShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the ticketDetailsList where type equals to UPDATED_TYPE
        defaultTicketDetailsShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllTicketDetailsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketDetailsRepository.saveAndFlush(ticketDetails);

        // Get all the ticketDetailsList where type is not null
        defaultTicketDetailsShouldBeFound("type.specified=true");

        // Get all the ticketDetailsList where type is null
        defaultTicketDetailsShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllTicketDetailsByTypeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ticketDetailsRepository.saveAndFlush(ticketDetails);

        // Get all the ticketDetailsList where type greater than or equals to DEFAULT_TYPE
        defaultTicketDetailsShouldBeFound("type.greaterOrEqualThan=" + DEFAULT_TYPE);

        // Get all the ticketDetailsList where type greater than or equals to UPDATED_TYPE
        defaultTicketDetailsShouldNotBeFound("type.greaterOrEqualThan=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllTicketDetailsByTypeIsLessThanSomething() throws Exception {
        // Initialize the database
        ticketDetailsRepository.saveAndFlush(ticketDetails);

        // Get all the ticketDetailsList where type less than or equals to DEFAULT_TYPE
        defaultTicketDetailsShouldNotBeFound("type.lessThan=" + DEFAULT_TYPE);

        // Get all the ticketDetailsList where type less than or equals to UPDATED_TYPE
        defaultTicketDetailsShouldBeFound("type.lessThan=" + UPDATED_TYPE);
    }


    @Test
    @Transactional
    public void getAllTicketDetailsByTypeStringIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketDetailsRepository.saveAndFlush(ticketDetails);

        // Get all the ticketDetailsList where typeString equals to DEFAULT_TYPE_STRING
        defaultTicketDetailsShouldBeFound("typeString.equals=" + DEFAULT_TYPE_STRING);

        // Get all the ticketDetailsList where typeString equals to UPDATED_TYPE_STRING
        defaultTicketDetailsShouldNotBeFound("typeString.equals=" + UPDATED_TYPE_STRING);
    }

    @Test
    @Transactional
    public void getAllTicketDetailsByTypeStringIsInShouldWork() throws Exception {
        // Initialize the database
        ticketDetailsRepository.saveAndFlush(ticketDetails);

        // Get all the ticketDetailsList where typeString in DEFAULT_TYPE_STRING or UPDATED_TYPE_STRING
        defaultTicketDetailsShouldBeFound("typeString.in=" + DEFAULT_TYPE_STRING + "," + UPDATED_TYPE_STRING);

        // Get all the ticketDetailsList where typeString equals to UPDATED_TYPE_STRING
        defaultTicketDetailsShouldNotBeFound("typeString.in=" + UPDATED_TYPE_STRING);
    }

    @Test
    @Transactional
    public void getAllTicketDetailsByTypeStringIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketDetailsRepository.saveAndFlush(ticketDetails);

        // Get all the ticketDetailsList where typeString is not null
        defaultTicketDetailsShouldBeFound("typeString.specified=true");

        // Get all the ticketDetailsList where typeString is null
        defaultTicketDetailsShouldNotBeFound("typeString.specified=false");
    }

    @Test
    @Transactional
    public void getAllTicketDetailsByCreatedTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketDetailsRepository.saveAndFlush(ticketDetails);

        // Get all the ticketDetailsList where createdTime equals to DEFAULT_CREATED_TIME
        defaultTicketDetailsShouldBeFound("createdTime.equals=" + DEFAULT_CREATED_TIME);

        // Get all the ticketDetailsList where createdTime equals to UPDATED_CREATED_TIME
        defaultTicketDetailsShouldNotBeFound("createdTime.equals=" + UPDATED_CREATED_TIME);
    }

    @Test
    @Transactional
    public void getAllTicketDetailsByCreatedTimeIsInShouldWork() throws Exception {
        // Initialize the database
        ticketDetailsRepository.saveAndFlush(ticketDetails);

        // Get all the ticketDetailsList where createdTime in DEFAULT_CREATED_TIME or UPDATED_CREATED_TIME
        defaultTicketDetailsShouldBeFound("createdTime.in=" + DEFAULT_CREATED_TIME + "," + UPDATED_CREATED_TIME);

        // Get all the ticketDetailsList where createdTime equals to UPDATED_CREATED_TIME
        defaultTicketDetailsShouldNotBeFound("createdTime.in=" + UPDATED_CREATED_TIME);
    }

    @Test
    @Transactional
    public void getAllTicketDetailsByCreatedTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketDetailsRepository.saveAndFlush(ticketDetails);

        // Get all the ticketDetailsList where createdTime is not null
        defaultTicketDetailsShouldBeFound("createdTime.specified=true");

        // Get all the ticketDetailsList where createdTime is null
        defaultTicketDetailsShouldNotBeFound("createdTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllTicketDetailsByTicketIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketDetailsRepository.saveAndFlush(ticketDetails);

        // Get all the ticketDetailsList where ticket equals to DEFAULT_TICKET
        defaultTicketDetailsShouldBeFound("ticket.equals=" + DEFAULT_TICKET);

        // Get all the ticketDetailsList where ticket equals to UPDATED_TICKET
        defaultTicketDetailsShouldNotBeFound("ticket.equals=" + UPDATED_TICKET);
    }

    @Test
    @Transactional
    public void getAllTicketDetailsByTicketIsInShouldWork() throws Exception {
        // Initialize the database
        ticketDetailsRepository.saveAndFlush(ticketDetails);

        // Get all the ticketDetailsList where ticket in DEFAULT_TICKET or UPDATED_TICKET
        defaultTicketDetailsShouldBeFound("ticket.in=" + DEFAULT_TICKET + "," + UPDATED_TICKET);

        // Get all the ticketDetailsList where ticket equals to UPDATED_TICKET
        defaultTicketDetailsShouldNotBeFound("ticket.in=" + UPDATED_TICKET);
    }

    @Test
    @Transactional
    public void getAllTicketDetailsByTicketIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketDetailsRepository.saveAndFlush(ticketDetails);

        // Get all the ticketDetailsList where ticket is not null
        defaultTicketDetailsShouldBeFound("ticket.specified=true");

        // Get all the ticketDetailsList where ticket is null
        defaultTicketDetailsShouldNotBeFound("ticket.specified=false");
    }

    @Test
    @Transactional
    public void getAllTicketDetailsByOrderNoIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketDetailsRepository.saveAndFlush(ticketDetails);

        // Get all the ticketDetailsList where orderNo equals to DEFAULT_ORDER_NO
        defaultTicketDetailsShouldBeFound("orderNo.equals=" + DEFAULT_ORDER_NO);

        // Get all the ticketDetailsList where orderNo equals to UPDATED_ORDER_NO
        defaultTicketDetailsShouldNotBeFound("orderNo.equals=" + UPDATED_ORDER_NO);
    }

    @Test
    @Transactional
    public void getAllTicketDetailsByOrderNoIsInShouldWork() throws Exception {
        // Initialize the database
        ticketDetailsRepository.saveAndFlush(ticketDetails);

        // Get all the ticketDetailsList where orderNo in DEFAULT_ORDER_NO or UPDATED_ORDER_NO
        defaultTicketDetailsShouldBeFound("orderNo.in=" + DEFAULT_ORDER_NO + "," + UPDATED_ORDER_NO);

        // Get all the ticketDetailsList where orderNo equals to UPDATED_ORDER_NO
        defaultTicketDetailsShouldNotBeFound("orderNo.in=" + UPDATED_ORDER_NO);
    }

    @Test
    @Transactional
    public void getAllTicketDetailsByOrderNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketDetailsRepository.saveAndFlush(ticketDetails);

        // Get all the ticketDetailsList where orderNo is not null
        defaultTicketDetailsShouldBeFound("orderNo.specified=true");

        // Get all the ticketDetailsList where orderNo is null
        defaultTicketDetailsShouldNotBeFound("orderNo.specified=false");
    }

    @Test
    @Transactional
    public void getAllTicketDetailsByWalletIsEqualToSomething() throws Exception {
        // Initialize the database
        Wallet wallet = WalletResourceIntTest.createEntity(em);
        em.persist(wallet);
        em.flush();
        ticketDetails.setWallet(wallet);
        ticketDetailsRepository.saveAndFlush(ticketDetails);
        Long walletId = wallet.getId();

        // Get all the ticketDetailsList where wallet equals to walletId
        defaultTicketDetailsShouldBeFound("walletId.equals=" + walletId);

        // Get all the ticketDetailsList where wallet equals to walletId + 1
        defaultTicketDetailsShouldNotBeFound("walletId.equals=" + (walletId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultTicketDetailsShouldBeFound(String filter) throws Exception {
        restTicketDetailsMockMvc.perform(get("/api/ticket-details?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ticketDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].userid").value(hasItem(DEFAULT_USERID.intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].addAmount").value(hasItem(DEFAULT_ADD_AMOUNT.booleanValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].typeString").value(hasItem(DEFAULT_TYPE_STRING.toString())))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(DEFAULT_CREATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].ticket").value(hasItem(DEFAULT_TICKET.intValue())))
            .andExpect(jsonPath("$.[*].orderNo").value(hasItem(DEFAULT_ORDER_NO.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultTicketDetailsShouldNotBeFound(String filter) throws Exception {
        restTicketDetailsMockMvc.perform(get("/api/ticket-details?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingTicketDetails() throws Exception {
        // Get the ticketDetails
        restTicketDetailsMockMvc.perform(get("/api/ticket-details/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTicketDetails() throws Exception {
        // Initialize the database
        ticketDetailsRepository.saveAndFlush(ticketDetails);
        int databaseSizeBeforeUpdate = ticketDetailsRepository.findAll().size();

        // Update the ticketDetails
        TicketDetails updatedTicketDetails = ticketDetailsRepository.findOne(ticketDetails.getId());
        // Disconnect from session so that the updates on updatedTicketDetails are not directly saved in db
        em.detach(updatedTicketDetails);
        updatedTicketDetails
            .userid(UPDATED_USERID)
            .amount(UPDATED_AMOUNT)
            .addAmount(UPDATED_ADD_AMOUNT)
            .type(UPDATED_TYPE)
            .typeString(UPDATED_TYPE_STRING)
            .createdTime(UPDATED_CREATED_TIME)
            .ticket(UPDATED_TICKET)
            .orderNo(UPDATED_ORDER_NO);
        TicketDetailsDTO ticketDetailsDTO = ticketDetailsMapper.toDto(updatedTicketDetails);

        restTicketDetailsMockMvc.perform(put("/api/ticket-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ticketDetailsDTO)))
            .andExpect(status().isOk());

        // Validate the TicketDetails in the database
        List<TicketDetails> ticketDetailsList = ticketDetailsRepository.findAll();
        assertThat(ticketDetailsList).hasSize(databaseSizeBeforeUpdate);
        TicketDetails testTicketDetails = ticketDetailsList.get(ticketDetailsList.size() - 1);
        assertThat(testTicketDetails.getUserid()).isEqualTo(UPDATED_USERID);
        assertThat(testTicketDetails.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testTicketDetails.isAddAmount()).isEqualTo(UPDATED_ADD_AMOUNT);
        assertThat(testTicketDetails.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTicketDetails.getTypeString()).isEqualTo(UPDATED_TYPE_STRING);
        assertThat(testTicketDetails.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testTicketDetails.getTicket()).isEqualTo(UPDATED_TICKET);
        assertThat(testTicketDetails.getOrderNo()).isEqualTo(UPDATED_ORDER_NO);
    }

    @Test
    @Transactional
    public void updateNonExistingTicketDetails() throws Exception {
        int databaseSizeBeforeUpdate = ticketDetailsRepository.findAll().size();

        // Create the TicketDetails
        TicketDetailsDTO ticketDetailsDTO = ticketDetailsMapper.toDto(ticketDetails);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTicketDetailsMockMvc.perform(put("/api/ticket-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ticketDetailsDTO)))
            .andExpect(status().isCreated());

        // Validate the TicketDetails in the database
        List<TicketDetails> ticketDetailsList = ticketDetailsRepository.findAll();
        assertThat(ticketDetailsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTicketDetails() throws Exception {
        // Initialize the database
        ticketDetailsRepository.saveAndFlush(ticketDetails);
        int databaseSizeBeforeDelete = ticketDetailsRepository.findAll().size();

        // Get the ticketDetails
        restTicketDetailsMockMvc.perform(delete("/api/ticket-details/{id}", ticketDetails.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TicketDetails> ticketDetailsList = ticketDetailsRepository.findAll();
        assertThat(ticketDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TicketDetails.class);
        TicketDetails ticketDetails1 = new TicketDetails();
        ticketDetails1.setId(1L);
        TicketDetails ticketDetails2 = new TicketDetails();
        ticketDetails2.setId(ticketDetails1.getId());
        assertThat(ticketDetails1).isEqualTo(ticketDetails2);
        ticketDetails2.setId(2L);
        assertThat(ticketDetails1).isNotEqualTo(ticketDetails2);
        ticketDetails1.setId(null);
        assertThat(ticketDetails1).isNotEqualTo(ticketDetails2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TicketDetailsDTO.class);
        TicketDetailsDTO ticketDetailsDTO1 = new TicketDetailsDTO();
        ticketDetailsDTO1.setId(1L);
        TicketDetailsDTO ticketDetailsDTO2 = new TicketDetailsDTO();
        assertThat(ticketDetailsDTO1).isNotEqualTo(ticketDetailsDTO2);
        ticketDetailsDTO2.setId(ticketDetailsDTO1.getId());
        assertThat(ticketDetailsDTO1).isEqualTo(ticketDetailsDTO2);
        ticketDetailsDTO2.setId(2L);
        assertThat(ticketDetailsDTO1).isNotEqualTo(ticketDetailsDTO2);
        ticketDetailsDTO1.setId(null);
        assertThat(ticketDetailsDTO1).isNotEqualTo(ticketDetailsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(ticketDetailsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(ticketDetailsMapper.fromId(null)).isNull();
    }
}
