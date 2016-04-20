package com.biodata.labguru.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.springframework.stereotype.Component;

import com.biodata.labguru.pages.enotebook.ExperimentPageV2;
import com.biodata.labguru.pages.enotebook.ProjectPage;
import com.biodata.labguru.pages.enotebook.ProtocolPageV2;
import com.biodata.labguru.pages.home.CalendarPage;
import com.biodata.labguru.pages.home.DashboardPage;
import com.biodata.labguru.pages.home.LabActivitiesPage;
import com.biodata.labguru.pages.home.RecentResultsPage;
import com.biodata.labguru.pages.inventory.ShoppingListPage;
import com.biodata.labguru.pages.inventory.purchasables.AntibodiesPage;
import com.biodata.labguru.pages.inventory.purchasables.BacteriaPage;
import com.biodata.labguru.pages.inventory.purchasables.CellLinePage;
import com.biodata.labguru.pages.inventory.purchasables.ConsumablesPage;
import com.biodata.labguru.pages.inventory.purchasables.FlyPage;
import com.biodata.labguru.pages.inventory.purchasables.FungiPage;
import com.biodata.labguru.pages.inventory.purchasables.LipidPage;
import com.biodata.labguru.pages.inventory.purchasables.TissuePage;
import com.biodata.labguru.pages.inventory.purchasables.VirusPage;
import com.biodata.labguru.pages.inventory.purchasables.WormPage;
import com.biodata.labguru.pages.inventory.purchasables.YeastPage;
import com.biodata.labguru.pages.inventory.purchasables.ZebrafishPage;
import com.biodata.labguru.pages.inventory.purchasables.botany.BotanyPlantsPage;
import com.biodata.labguru.pages.inventory.purchasables.botany.BotanySeedsPage;
import com.biodata.labguru.pages.inventory.purchasables.sequenceable.GenePage;
import com.biodata.labguru.pages.inventory.purchasables.sequenceable.GenericCollectionPage;
import com.biodata.labguru.pages.inventory.purchasables.sequenceable.PlasmidsPage;
import com.biodata.labguru.pages.inventory.purchasables.sequenceable.PrimersPage;
import com.biodata.labguru.pages.inventory.purchasables.sequenceable.ProteinPage;
import com.biodata.labguru.pages.inventory.purchasables.sequenceable.SequencePage;
import com.biodata.labguru.pages.inventory.rodents.RodentCagesPage;
import com.biodata.labguru.pages.inventory.rodents.RodentSpecimensPage;
import com.biodata.labguru.pages.inventory.rodents.RodentStrainsPage;
import com.biodata.labguru.pages.inventory.rodents.RodentTreatmentPage;
import com.biodata.labguru.pages.knowledgebase.DocumentPage;
import com.biodata.labguru.pages.knowledgebase.ImagePage;
import com.biodata.labguru.pages.knowledgebase.PaperPage;
import com.biodata.labguru.pages.knowledgebase.RecipePage;
import com.biodata.labguru.pages.knowledgebase.SOPPage;
import com.biodata.labguru.pages.storage.BoxPage;
import com.biodata.labguru.pages.storage.CryogenicCanePage;
import com.biodata.labguru.pages.storage.EquipmentPage;
import com.biodata.labguru.pages.storage.StockPage;
import com.biodata.labguru.pages.storage.StoragePage;



/**
 * Created by Goni
 * Manages all the web pages.
 */
@Component
public class PageManager  {

	private WebDriver wdriver;

	private Logger logger;


	public void init( WebDriver driver){

		wdriver = driver;  		
		//set the screen dimention to work well from jenkins (on ubuntu server using xvfb)
		getWebDriver().manage().window().setSize(new Dimension(1440,755));
		adminPage = new AdminPage();
		loginPage = new LoginPage();
		accountSettingPage = new AccountSettingsPage();

		dashboardPage = new DashboardPage();
		labActivitiesPage = new LabActivitiesPage(); 
		recentResultsPage = new RecentResultsPage();
		calendarPage = new CalendarPage();


		boxPage = new BoxPage();
		cryogenicPage = new CryogenicCanePage();
		stockPage = new StockPage();
		equipmentPage = new EquipmentPage();
		storagePage = new StoragePage();

		projectPage = new ProjectPage();
		protocolPage = new ProtocolPageV2();
		experimentPage = new ExperimentPageV2();


		paperPage = new PaperPage();
		documentPage = new DocumentPage();
		recipePage = new RecipePage();
		imagePage = new ImagePage();

		shoppingListPage = new ShoppingListPage();
		consumablesPage = new ConsumablesPage();
		antibodiesPage = new AntibodiesPage();
		plasmidsPage = new PlasmidsPage();
		bacteriaPage = new BacteriaPage();
		cellLinePage = new CellLinePage();
		primersPage = new PrimersPage();
		proteinPage = new ProteinPage();
		lipidPage = new LipidPage();
		flyPage = new FlyPage();
		tissuePage = new TissuePage();
		fungiPage = new FungiPage();
		wormPage = new WormPage();
		genePage = new GenePage();
		sequencePage = new SequencePage();
		virusPage = new VirusPage();
		yeastPage = new YeastPage();
		zebrafishPage = new ZebrafishPage();
		botanyPlantsPage = new BotanyPlantsPage();
		botanySeedsPage = new BotanySeedsPage();
		rodentSpecimensPage = new RodentSpecimensPage();
		rodentStrainsPage = new RodentStrainsPage();
		cagesPage = new RodentCagesPage();
		treatmentPage = new RodentTreatmentPage();
		genericCollectionPage = new GenericCollectionPage();
	}

	public WebDriver getWebDriver() {
		return wdriver;
	}

	public Logger getLogger() {
		return logger;
	}

	public String getBrowserType() {
		return this.browserType;
	}

	public void setBrowserType(String browser,WebDriver driver) {
		this.browserType = browser;
		init(driver);
	}

	public AdminPage adminPage;

	private LoginPage loginPage;

	private LabActivitiesPage labActivitiesPage;

	private DashboardPage dashboardPage;

	private AccountSettingsPage accountSettingPage;	

	private RecentResultsPage recentResultsPage;

	private CalendarPage calendarPage;

	private ExperimentPageV2 experimentPage;

	private ProjectPage projectPage;

	private MembersPage membersPage;

	private ShoppingListPage shoppingListPage;


	private ConsumablesPage consumablesPage;

	private PlasmidsPage plasmidsPage;

	private AntibodiesPage antibodiesPage;


	private CellLinePage cellLinePage;


	private PrimersPage primersPage;


	private BacteriaPage bacteriaPage;


	private FlyPage flyPage;


	private FungiPage fungiPage;


	private GenePage genePage;


	private LipidPage lipidPage;


	private ProteinPage proteinPage;

	private SequencePage sequencePage;	


	private TissuePage tissuePage;


	private VirusPage virusPage;


	private WormPage wormPage;


	private YeastPage yeastPage;


	private ZebrafishPage zebrafishPage;



	private RodentSpecimensPage rodentSpecimensPage;


	private RodentStrainsPage rodentStrainsPage;


	private RodentCagesPage cagesPage;


	private RodentTreatmentPage treatmentPage;


	private BotanyPlantsPage botanyPlantsPage;


	private BotanySeedsPage botanySeedsPage;


	private ProtocolPageV2 protocolPage;


	private PaperPage paperPage;


	private RecipePage recipePage;	


	private DocumentPage documentPage;
	
	private ImagePage imagePage;


	private SOPPage sopPage;


	private BoxPage boxPage;


	private CryogenicCanePage cryogenicPage;


	private StoragePage storagePage;


	private EquipmentPage equipmentPage;


	private StockPage stockPage;


	private GenericCollectionPage genericCollectionPage;

	private String otherUrl;

	private String productionUrl;

	private String stagingUrl;

	private String browserType;

	private String gmailUrl;

	private String gmailAccount;

	private String gmailPassword;




	public String getGmailAccount() {
		return gmailAccount;
	}

	public void setGmailAccount(String gmailAccount) {
		this.gmailAccount = gmailAccount;
	}

	public String getGmailPassword() {
		return gmailPassword;
	}

	public void setGmailPassword(String gmailPassword) {
		this.gmailPassword = gmailPassword;
	}

	public String getGmailUrl() {
		return gmailUrl;
	}

	public void setGmailUrl(String gmailUrl) {
		this.gmailUrl = gmailUrl;
	}

	public String getOtherUrl() {
		return otherUrl;
	}

	public void setOtherUrl(String otherUrl) {
		this.otherUrl = otherUrl;
	}



	public String getProductionUrl() {
		return productionUrl;
	}

	public void setProductionUrl(String productionUrl) {
		this.productionUrl = productionUrl;
	}

	public String getStagingUrl() {
		return stagingUrl;
	}

	public void setStagingUrl(String stagingUrl) {
		this.stagingUrl = stagingUrl;
	}

	public AccountSettingsPage getAccountSettingPage() {
		return accountSettingPage;
	}

	public LoginPage getLoginPage() {
		return loginPage;
	}

	public DashboardPage getDashboardPage() {
		return dashboardPage;
	}

	public LabActivitiesPage getLabActivitiesPage() {
		return labActivitiesPage;
	}

	public RecentResultsPage getRecentResultsPage() {
		return recentResultsPage;
	}

	public AdminPage getAdminPage() {
		return adminPage;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public void initPages(WebDriver remoteWebDriver,Logger logger) {
		initPage(adminPage,remoteWebDriver,logger);
		initPage(loginPage,remoteWebDriver,logger);
		initPage(accountSettingPage,remoteWebDriver,logger);
		initPage(dashboardPage,remoteWebDriver,logger);
		initPage(labActivitiesPage,remoteWebDriver,logger);
		initPage(calendarPage,remoteWebDriver,logger);
		initPage(recentResultsPage,remoteWebDriver,logger);

		initPage(boxPage,remoteWebDriver,logger);
		initPage(cryogenicPage,remoteWebDriver,logger);
		initPage(storagePage,remoteWebDriver,logger);
		initPage(stockPage,remoteWebDriver,logger);
		initPage(equipmentPage,remoteWebDriver,logger);


		initPage(projectPage,remoteWebDriver,logger);
		initPage(protocolPage,remoteWebDriver,logger);
		initPage(experimentPage,remoteWebDriver,logger);


		initPage(paperPage,remoteWebDriver,logger);
		initPage(documentPage,remoteWebDriver,logger);
		initPage(recipePage,remoteWebDriver,logger);
		initPage(imagePage,remoteWebDriver,logger);

		initPage(shoppingListPage,remoteWebDriver,logger);
		initPage(consumablesPage,remoteWebDriver,logger);
		initPage(antibodiesPage,remoteWebDriver,logger);
		initPage(bacteriaPage,remoteWebDriver,logger);
		initPage(cellLinePage,remoteWebDriver,logger);
		initPage(plasmidsPage,remoteWebDriver,logger);
		initPage(primersPage,remoteWebDriver,logger);
		initPage(proteinPage,remoteWebDriver,logger);
		initPage(tissuePage,remoteWebDriver,logger);
		initPage(fungiPage,remoteWebDriver,logger);
		initPage(yeastPage,remoteWebDriver,logger);
		initPage(lipidPage,remoteWebDriver,logger);
		initPage(genePage,remoteWebDriver,logger);
		initPage(sequencePage,remoteWebDriver,logger);
		initPage(wormPage,remoteWebDriver,logger);
		initPage(zebrafishPage,remoteWebDriver,logger);
		initPage(flyPage,remoteWebDriver,logger);
		initPage(botanyPlantsPage,remoteWebDriver,logger);
		initPage(botanySeedsPage,remoteWebDriver,logger);
		initPage(rodentSpecimensPage,remoteWebDriver,logger);
		initPage(rodentStrainsPage,remoteWebDriver,logger);
		initPage(cagesPage,remoteWebDriver,logger);
		initPage(treatmentPage,remoteWebDriver,logger);
		initPage(virusPage,remoteWebDriver,logger);
		initPage(genericCollectionPage,remoteWebDriver,logger);


	}

	protected void initPage(BasePage page,WebDriver remoteWebDriver,Logger logger) {
		page.initPage(remoteWebDriver);
		page.setLogger(logger);
		page.setWebDriver(remoteWebDriver);
		page.setBrowserType(getBrowserType());
	}


	public ExperimentPageV2 getExperimentPage() {
		PageFactory.initElements(getWebDriver(), ExperimentPageV2.class);
		return experimentPage;
	}

	public ProjectPage getProjectPage() {
		return projectPage;
	}


	public CalendarPage getCalendarPage() {
		PageFactory.initElements(getWebDriver(), CalendarPage.class);
		return calendarPage;
	}


	public ShoppingListPage getShoppingListPage() {
		return shoppingListPage;
	}

	public ConsumablesPage getConsumablesPage() {
		return consumablesPage;
	}

	public PlasmidsPage getPlasmidsPage() {
		return plasmidsPage;
	}

	public MembersPage getMembersPage() {
		return membersPage;
	}

	public ProtocolPageV2 getProtocolPage() {
		PageFactory.initElements(getWebDriver(), ProtocolPageV2.class);
		return protocolPage;
	}

	public BoxPage getBoxPage() {
		return boxPage;
	}

	public CryogenicCanePage getCryogenicPage() {
		return cryogenicPage;
	}

	public StockPage getStockPage() {
		return stockPage;
	}

	public PaperPage getPaperPage() {
		return paperPage;
	}

	public DocumentPage getDocumentPage() {
		return documentPage;
	}

	public RecipePage getRecipePage() {
		return recipePage;
	}
	
	public ImagePage getImagePage(){
		return imagePage;
	}

	public SOPPage getSOPPage() {
		return sopPage;
	}

	public AntibodiesPage getAntibodiesPage() {

		return antibodiesPage;
	}

	public CellLinePage getCellLinesPage() {

		return cellLinePage;
	}

	public RodentSpecimensPage getRodentSpecimensPage() {

		return rodentSpecimensPage;
	}

	public RodentStrainsPage getRodentStrainsPage() {

		return rodentStrainsPage;
	}

	public RodentCagesPage getCagesPage() {
		return cagesPage;
	}

	public RodentTreatmentPage getTreatmentPage() {
		return treatmentPage;
	}

	public BotanyPlantsPage getBotanyPlantsPage() {

		return botanyPlantsPage;
	}

	public BotanySeedsPage getBotanySeedsPage() {

		return botanySeedsPage;
	}

	public BacteriaPage getBacteriaPage() {

		return bacteriaPage;
	}

	public PrimersPage getPrimersPage() {

		return primersPage;
	}

	public FlyPage getFlyPage() {
		return flyPage;
	}

	public FungiPage getFungiPage() {
		return fungiPage;
	}

	public GenePage getGenePage() {
		return genePage;
	}

	public LipidPage getLipidPage() {
		return lipidPage;
	}

	public ProteinPage getProteinPage() {
		return proteinPage;
	}

	public SequencePage getSequencePage() {
		return sequencePage;
	}

	public TissuePage getTissuePage() {
		return tissuePage;
	}

	public VirusPage getVirusPage() {
		return virusPage;
	}

	public WormPage getWormPage() {
		return wormPage;
	}

	public YeastPage getYeastPage() {
		return yeastPage;
	}

	public ZebrafishPage getZebrafishPage() {
		return zebrafishPage;
	}

	public StoragePage getStoragePage() {
		return storagePage;

	}

	public EquipmentPage getEquipmentPage() {
		return equipmentPage;

	}

	public GenericCollectionPage getGenericCollectionPage() {
		return genericCollectionPage;
	}

	public String getPlatform() {
		Platform platform = ((RemoteWebDriver)wdriver).getCapabilities().getPlatform();
		return platform.name();
	}

}