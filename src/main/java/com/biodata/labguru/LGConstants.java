package com.biodata.labguru;

import java.util.Arrays;
import java.util.List;



public final class LGConstants {
	
	public static final String WOOOPS_MESSAGE = "Whooooops";
	
	public static final String NOTY_TEXT_CLASS_NAME = ".noty_text";
	
	public static final String GMAIL_SUFFIX_MAIL = "@gmail.com";
	public static final String QA_PREFIX_MAIL = "labguru.qa+";
	public static final String BIODATA_MEMBER_NAME = "automation";
	public static final String STRONG_PASSWORD = "Test1234";
	public static final String JENKINS = "jen_";
	public static final String NEW_ACCOUNT_PREFIX = "labguru.qa+new";
	
	//gmail account to check mails
	public static final String GMAIL_URL = "https://accounts.google.com/ServiceLogin?";
	public static final String GMAIL_ACCOUNT ="labguru.qa@gmail.com";
	public static final String GMAIL_ACCOUNT_PASS ="labguru 2015";
	
	
	
	public static final String MATERIALS = "materials";
	public static final String CONSUMABLES_MENU = "consumables";
	public static final String PLASMIDS = "Plasmids";
	public static final String ANTIBODIES = "Antibodies";
	public static final String CELL_LINES = "Cell_lines";
	public static final String CELL_LINES_MSG = "cell lines";
	public static final String PRIMERS = "Primers";
	public static final String BACTERIA = "Bacteria";
	public static final String RODENTS = "Rodents";
	public static final String BOTANY = "Botany";
	public static final String SEQUENCES = "Sequences";
	public static final String PROTEINS = "Proteins";
	public static final String FLIES = "Flies";
	public static final String FUNGI = "Fungi";
	public static final String WORMS = "Worms";
	public static final String VIRUSES = "Viruses";
	public static final String ZEBRAFISHES = "Zebrafishes";
	public static final String GENES = "Genes";
	public static final String LIPIDS = "Lipids";
	public static final String TISSUES = "Tissues";
	public static final String YEASTS = "Yeasts";
	public static final String EXPERIMENT = "Experiment";
	public static final String RODENTS_SPECIMEN = "rodent specimen";
	public static final String RODENTS_STRAIN = "rodent strains";
	public static final String BOTANY_PLANT = "Plants";
	public static final String BOTANY_SEED = "Seeds";
	
	public static final String RODENTS_SPECIMEN_CREATION_TITLE = "Rodent specimen";
	public static final String RODENTS_STRAIN_CREATION_TITLE = "Rodent strain";
	public static final String RODENTS_CAGE_CREATION_TITLE = "Rodent cage";
	public static final String BOTANY_PLANT_CREATION_TITLE = "Plant";
	public static final String BOTANY_SEED_CREATION_TITLE = "Seed";
	
	
	//prefixs
	public static final String PROJECT_PREFIX = "project_";
	public static final String EXPERIMENT_PREFIX = "experiment_";
	public static final String FOLDER_PREFIX = "folder_";
	public static final String PLASMID_PREFIX = "plasmid_";
	public static final String FLIES_PREFIX = "fly_";
	public static final String PROTEIN_PREFIX = "protein_";
	public static final String GENE_PREFIX = "gene_";
	public static final String YEAST_PREFIX = "yeast_";
	public static final String TISSUE_PREFIX = "tissue_";
	public static final String VIRUS_PREFIX = "virus_";
	public static final String WORM_PREFIX = "worm_";
	public static final String ZEBRAFISH_PREFIX = "zebrafish_";
	public static final String SEQUENCE_PREFIX = "sequence_";
	public static final String FUNGI_PREFIX = "fungus_";
	public static final String LIPID_PREFIX = "lipid_";
	public static final String ANTIBODY_PREFIX = "antibody_";
	public static final String RECIPE_PREFIX = "recipe_";
	public static final String SAMPLE_PREFIX = "sample_";
	public static final String BOX_CELLINE_PREFIX = "boxCellLine_";
	public static final String BOX_TOSHOW_PREFIX = "boxToShow_";
	public static final String BOX_WITH_STOCK_PREFIX = "boxWithStock_";
	public static final String BOX_PREFIX = "box_";
	public static final String STOCK_PREFIX = "stock_";
	public static final String EVENT_PREFIX = "event_";
	public static final String TAG_PREFIX = "tag_";
	public static final String TASK_PREFIX = "task_";
	public static final String PAPER_PREFIX = "paper_";
	public static final String DOCUMENT_PREFIX = "document_";
	public static final String CONSUMABLE_PREFIX = "material_";
	public static final String CRYOGENIC_CANE_PREFIX = "cane_";
	public static final String MESSAGE_PREFIX = "message_";
	public static final String PROTOCOL_PREFIX = "protocol_";
	public static final String PRIMER_PREFIX = "primer_";
	public static final String CELL_LINE_PREFIX = "cell_line_";
	public static final String BACTERIA_PREFIX = "bacterium_";
	public static final String RODENT_SPECIMEN_PREFIX = "specimen_";
	public static final String RODENT_STRAIN_PREFIX = "strain_";
	public static final String BOTANY_PLANT_PREFIX = "plant_";
	public static final String BOTANY_SEED_PREFIX = "seed_";
	public static final String CAGE_PREFIX = "cage_";
	public static final String TREATMENT_PREFIX = "treatment_";
	public static final String STORAGE_PREFIX = "storage_";
	public static final String EQUIPMENT_PREFIX = "equipment_";
	public static final String GENERIC_PREFIX = "generic_";
	public static final String SOP_PREFIX = "SOP_";
	
	
	
	//collections
	public static final String CUSTOM_FIELD_PREDEFIND_LIST = "Pre-defined list";
	public static final String CUSTOM_FIELD_PREDEFIND_LIST_MULTI = "Pre-defined list with multiple select";
	public static final String GENERIC_COLLECTION_PREFIX = "gen_";
	public static final String GENERIC_COLLECTION_MENU_ID = "gen_menu";
	public static final String GENERIC_COLLECTION_NAME= "gen";
	public static final String GENERIC_COLLECTION_PAGE_TITLE_PREFIX = "Customise";
	public static final String CUSTOM_FIELD_PREFIX = "f_";
	public static final String CUSTOM_FIELD_ONLY_NUMBERS = "Only numbers";
	public static final String CUSTOM_FIELD_DATE = "Date";
	public static String[] CUSTOM_FIELD_TYPES_ARRAY = new String[]{"Regular field (text and numbers)",
		CUSTOM_FIELD_ONLY_NUMBERS,"URL","Text area",CUSTOM_FIELD_DATE,CUSTOM_FIELD_PREDEFIND_LIST/*,CUSTOM_FIELD_PREDEFIND_LIST_MULTI*/};
	
	
	public static final String TUBE = "Tube";
	public static final String BOTTLE = "Bottle";
	public static final String PACKAGE = "Package";
	public static final String VIAL = "Vial";
	public static final String SLIDE = "Slide";
	public static final String BLOCK = "Block";
	public static final String ENVELOPE = "Envelope";
	public static final String OTHER = "Other";
	public static String[] STOCK_TYPES_ARRAY = new String[]{TUBE,PACKAGE,BOTTLE,VIAL,SLIDE,BLOCK,ENVELOPE,OTHER};
	
	
	
	public static final String ROOM = "Room";
	public static final String SHELF = "Shelf";
	public static final String CLOSET = "Closet";
	public static final String DRAWER = "Drawer";
	public static final String REFRIGERATOR = "Refrigerator";
	public static final String FREEZER = "Freezer";
	public static final String CRYO_CONTAINER = "Cryo container";
	public static final String VERTICAL_RACK = "Vertical Rack";
	public static final String SLIDE_RACK = "Slide Rack";
	public static final String HORIZONTAL_RACK = "Horizontal Rack";

	public static String[] STORAGE_TYPES_ARRAY = new String[]{ROOM,SHELF,CLOSET,DRAWER,REFRIGERATOR,FREEZER,
		CRYO_CONTAINER,VERTICAL_RACK,SLIDE_RACK,HORIZONTAL_RACK,OTHER};

	
	//shopping list buttons labels
	public static final String SUBMIT_ORDER = "Submit Order";
	public static final String MARK_AS_ARRIVED = "Mark As Arrived";

	public static final String UPLOAD_TXT_TEST_FILENAME = "myTest.txt";
	public static final String UPLOAD_IMAGE_TEST_FILENAME = "Pointy.gif";
	public static final String UPLOAD_PDF_TEST_FILENAME = "ab1.pdf";
	public static final String UPLOAD_XLS_TEST_FILENAME = "zones.xls";
	
	public static final String ASSETS_FILES_DIRECTORY = "/Assets/";
	public static final String COLLECTIONS_TEMPLATES_DIRECTORY = "CollectionsTemplates";
	public static final String COLLECTIONS_IMPORT_DIRECTORY = COLLECTIONS_TEMPLATES_DIRECTORY + "/CollectionsFilesToImport";
	public static final String REACTION_FILES_DIRECTORY = "chemaxon_reaction_library/";
	public static final String REACTION_FILE_TO_IMPORT = "Benzoxazole formation from 2-aminophenol and carbonyls.mrv";
	public static final String OUTPUT_EXCEL_FILE = "output.xls";
	public static final String SCREENSHOTS_DIRECTORY = "Screenshots/";
	public static final String CALENDAR_FORMAT = "MMMMM dd, yyyy";
	public static final String VERSIONS_HISTORY_CALENDAR_FORMAT = "yyyy-MM-dd";
	
	//biocollections templates
	public static final String LIPIDS_TEMPLATE = "Lipids_Template.xls";
	public static final String FLIES_TEMPLATE = "Flies_Template.xls";
	public static final String FUNGUS_TEMPLATE = "Fungus_Template.xls";
	public static final String GENES_TEMPLATE = "Genes_Template.xls";
	public static final String PROTEINS_TEMPLATE = "Proteins_Template.xls";
	public static final String TISSUES_TEMPLATE = "Tissues_Template.xls";
	public static final String VIRUSES_TEMPLATE = "Viruses_Template.xls";
	public static final String WORMS_TEMPLATE = "Worms_Template.xls";
	public static final String ZEBRFISHES_TEMPLATE = "Zebrafishes_Template.xls";
	public static final String YEASTS_TEMPLATE = "Yeasts_Template.xls";	
	public static final String PLASMIDS_TEMPLATE = "Plasmid_Template.xls";
	public static final String CONSUMABLES_TEMPLATE = "Material_Template.xls";
	public static final String ANTIBODIES_TEMPLATE = "Antibody_Template.xls";
	public static final String PRIMERS_TEMPLATE = "Primer_Template.xls";
	public static final String BACTERIA_TEMPLATE = "Bacterium_Template.xls";
	public static final String CELLLINE_TEMPLATE = "Cell_lines_Template.xls";
	public static final String RODENT_SPECIMEN_TEMPLATE = "rodent_specimen_Template.xls";
	public static final String RODENT_STRAIN_TEMPLATE = "rodent_strains_Template.xls";
	public static final String BOTANY_PLANTS_TEMPLATE = "Plant_Template.xls";
	public static final String BOTANY_SEEDS_TEMPLATE = "Seed_Template.xls";
	public static final String GENERIC_COLLECTION_TEMPLATE1 = "Generic_Template1.xls";
	public static final String GENERIC_COLLECTION_TEMPLATE2 = "Generic_Template2.xls";

	//browsers
	public static final String FIREFOX = "firefox";
	public static final String CHROME = "chrome";

	public static final String SETTINGS_LABEL = "Settings";
	public static final String CUSTOMIZE_LABEL = "Customize";

	public static final String TUBE_DEFAULT_LOCATION = "A1";


	//treatments
	public static final String TODAY = "today";
	public static final String TOMORROW = "tomorrow";
	public static final String TREATMENT_GREEN_BACKGROUND = "treatment  green_background";
	public static final String TREATMENT_RED_BACKGROUND = "purple_background treatment";
	public static final String TREATMENT_WHITE_BACKGROUND = "treatment";

	public static final String JERUSALEM_TIME_ZONE = "Jerusalem";

	public static final String OUTPUT_REPORT_DIR = "TestsReports";

	public static final String SPECIMEN_STATUS_ALIVE = "Alive";
	public static final String SPECIMEN_STATUS_DEAD = "Dead";
	public static final String SPECIMEN_STATUS_MISSING = "Missing";
	public static final String SPECIMEN_STATUS_SACRIFICED = "Sacrificed";

	public static final String IMPORTED_NAME = "imported";

	public static final String EXPERIMENT_CURRENT = "Current";
	public static final String EXPERIMENT_BETA = "Beta";

	public static final List<String> DEFAULT_COLLECTIONS = Arrays.asList(PLASMIDS,CELL_LINES,PRIMERS,ANTIBODIES,RODENTS,BACTERIA);

	public static final String ANTIBODY = "antibody";

	public static final String SECTION_PREFIX = "Section_";

	public static final String NEW_EXP_FROM_PROTOCOL = "new_experiment_from_protocol_link";
	public static final String NEW_DOCUMENT = "new_document_from_popup";
	public static final String NEW_PROJECT = "new_project_from_popup";
	public static final String NEW_PROTOCOL = "new_protocol_from_popup";

	public static final String PAGE_NOT_FOUND = "Page not found";

	//Generic custom fields
	public static final String PURCHASABLE_ATTRIBUTES_FIELD = "Purchasable Attributes";
	public static final String SEQUENCE_FIELD = "Sequence";





	//calendar days
	public enum Day{

		SUNDAY(1),
		MONDAY(2),
		TUESDAY(3),
		WEDNESDAY(4),
		THURSDAY(5),
		FRIDAY(6),
		SATURDAY(7);
		
		private int value;
		
		private Day(int value) {
			this.value = value;
		}		
	
		
		public int getValue() {
			return this.value;
		}
		public String getShortDay(int day) {
			switch(this) {
			   case SUNDAY:			
		            return "sun";			
		        case MONDAY:		
		            return "mon";			
		        case TUESDAY:			
		            return "tue";			
		        case WEDNESDAY:			
		            return "wed";			
		        case THURSDAY:			
		            return "thu";			
		        case FRIDAY:			
		            return "fri";			
		        case SATURDAY:			
		            return "sat";			
		        default:			
			            return null;
			}
 
		}
	}
}
