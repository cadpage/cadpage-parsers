package net.anei.cadpage.parsers.MN;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA41Parser;

public class MNItascaCountyParser extends DispatchA41Parser {

  public MNItascaCountyParser() {
    this("ITASCA COUNTY", "MN");
  }

  public MNItascaCountyParser(String defCity, String defState) {
    super(CITY_CODES, defCity, defState, "[67][A-Z0-9]{6,7}");
  }

  @Override
  public String getAliasCode() {
    return "MNItascaCounty";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {

    // Fixed IAR edits :(
    body = body.replace("\n \n\n ", ", mappage,XXXX, ").replace("\n ", ", ");

    if (!super.parseMsg(body, data)) return false;

    // They use things a bit differently
    data.strMap = data.strChannel;
    data.strChannel = data.strApt;
    data.strApt = "";

    if (data.strCity.equals("Superior")) data.strState = "WI";
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("CH", "MAP").replace("APT", "CH").replace("CITY", "CITY ST");
  }

  @Override
  public String adjustMapCity(String city) {
    if (city.toUpperCase().startsWith("UNORG")) return "";
    return city;
  }

  private static Properties CITY_CODES = buildCodeTable(new String[]{

      // St Louis County
      "101",    "UNORG 52-21",
      "102",    "UNORG 53-15",
      "103",    "UNORG 53-16",
      "104",    "UNORG 54-13",
      "105",    "UNORG 54-14/15 62&63",
      "106",    "",     // New Cad Twp
      "107",    "UNORG T55-R14",
      "108",    "UNORG T55-R15",
      "1AB",    "Alborn Twp",
      "1AD",    "Alden Twp",
      "1AL",    "Ault Twp",
      "1AR",    "Arrowhead Twp",
      "1BR",    "Brookston",
      "1BS",    "Bassett",
      "1BV",    "Brevator",
      "1CA",    "Canosia Twp",
      "1CR",    "Cherry",
      "1CT",    "Cotton Twp",
      "1CU",    "Culver Twp",
      "1CV",    "Cedar Valley Twp",
      "1DL",    "Duluth Twp",
      "1DU",    "Duluth",
      "1EB",    "Ellsburg",
      "1ER",    "Elmer Twp",
      "1FB",    "Fairbanks Twp",
      "1FD",    "Fond Du Lac Forest",
      "1FE",    "Fredenberg Twp",
      "1FL",    "Floodwood",
      "1FN",    "Fine Lakes Twp",
      "1FW",    "Floodwood Twp",
      "1GL",    "Grand Lake Twp",
      "1GN",    "Gnesen Twp",
      "1HA",    "Halden Twp",
      "1HE",    "Hermantown",
      "1IN",    "Industrial Twp",
      "1KE",    "Kelsey Twp",
      "1LV",    "Lavell Twp",
      "1LW",    "Lakewood Twp",
      "1MD",    "McDavitt Twp",
      "1ME",    "Meadowlands",
      "1MI",    "Midway Twp",
      "1ML",    "Meadowlands Twp",
      "1NE",    "Ness Twp",
      "1NI",    "New Independence",
      "1NL",    "Northland Twp",
      "1NO",    "Normanna Twp",
      "1NS",    "North Star Twp",
      "1PL",    "Prairie Lake Twp",
      "1PQ",    "Pequaywan Twp",
      "1PR",    "Proctor",
      "1RL",    "Rice Lake Twp",
      "1SB",    "Stoney Brook Twp",
      "1SU",    "Superior",                // In Wisconsin
      "1SW",    "Solway Twp",
      "1TV",    "Toivola Twp",
      "1UM",    "UMD",
      "1VB",    "Van Buren Twp",

      // Cook County
      "2BWCA ", "BWCA",                   // ???
      "2CAR-LUT","Caribou Lake-Lutsen",   // ???
      "2COL",   "Colvill",
      "2DEVIL", "Devil Track Lake",       // ???
      "2GGM",   "Rosebush, Creechville, Croftville", // ???
      "2GM",    "Grand Marais",
      "2GP",    "Grand Portage",
      "2GRN",   "Greenwood Lake",         // ??
      "2GUNFL", "Gunflint/Loon Lakes",    // ??
      "2HOV",   "Hovland",
      "2LUT",   "Lutsen Twp",
      "2MCF",   "McFarland Lake",
      "2MGUNF", "Mid Gunflint Trail",    // ??
      "2MPH",   "Maple Hill Twp",        // ??
      "2PIKE ", "Pike Lake",             // ??
      "2SAG",   "Seagull/Sage Lakes",    // ??
      "2SCH",   "Schroeder",
      "2TOF",   "Tofte Twp",
      "2UNO",   "Unorganized",

      // Lake County && St Louis County
      "3BB",    "Beaver Bay",
      "3BRI",   "Brimson",
      "3ELY",   "Ely",
      "3FIN",   "Finland",
      "3ISA",   "Isabella",
      "3KR",    "Knife River",
      "3LK",    "Lake County",
      "3SIL",   "Silver Bay",
      "3TH",    "Two Harbors",

      // Koochiching County & Lake of the Woods County & Itsaca County
      "4-Mar ", "Margie",
      "4BAU",   "Baudette",
      "4BF",    "Big Falls",
      "4BI",    "Birchdale",
      "4EFF",   "Effie",
      "4ERI",   "Ericsburg",
      "4IF",    "International Falls",
      "4IV",    "Island View",
      "4KAB",   "Kabetogama",
      "4KSO",   "Koochiching County",
      "4LF",    "Littlefork",
      "4LOM",   "Loman",
      "4MIZ",   "Mizpah",
      "4NET",   "Nett Lake",
      "4NOR",   "Northome",
      "4RAI",   "Ranier",
      "4RAY",   "Ray",
      "4WAS",   "Waskish",

      // Pine County
      "5001",   "Birch Creek Twp",
      "5002",   "Sturgeon Lake Twp",
      "5003",   "Windemere Twp",
      "5004",   "Kerrick Twp",
      "5005",   "Nickerson Twp",
      "5006",   "Bremen Twp",
      "5007",   "Kettle River Twp",
      "5008",   "Norman Twp",
      "5009",   "Bruno Twp",
      "5010",   "Park Twp",
      "5011",   "New Dosey Twp",
      "5012",   "Pine Lake Twp",
      "5013",   "Finlayson Twp",
      "5014",   "Partridge Twp",
      "5015",   "Fleming Twp",
      "5016",   "Dell Grove Twp",
      "5017",   "Sandstone Twp",
      "5018",   "Danforth Twp",
      "5019",   "Wilma Twp",
      "5020",   "Arna Twp",
      "5021",   "Hinckley Twp",
      "5022",   "Barry Twp",
      "5023",   "Arlone Twp",
      "5024",   "Clover Twp",
      "5025",   "Ogema Twp",
      "5026",   "Brook Park Twp",
      "5027",   "Mission Creek Twp",
      "5028",   "Munch Twp",
      "5029",   "Crosby Twp",
      "5030",   "Pokegama Twp",
      "5031",   "Chengwatana Twp",
      "5032",   "Royalton Twp",
      "5033",   "Pine City Twp",
      "5101",   "Askov",
      "5102",   "Brook Park",
      "5103",   "Bruno",
      "5104",   "Denham",
      "5105",   "Finlayson",
      "5106",   "Henriette",
      "5107",   "Hinckley",
      "5108",   "Kerrick",
      "5109",   "Pine City",
      "5110",   "Rock Creek",
      "5111",   "Rutledge",
      "5112",   "Sandstone",
      "5113",   "Sturgeon Lake",
      "5114",   "Willow River",
      "5AS",    "Askov",
      "5BN",    "Beroun",
      "5BP",    "Brook Park",
      "5BR",    "Bruno",
      "5BRA",   "Braham",
      "5DN",    "Denham",
      "5DQ",    "Duquette",
      "5DX",    "Duxbury",
      "5FI",    "Finlayson",
      "5GS",    "Grasston",   // In Kanabec County
      "5HE",    "Henriette",
      "5HK",    "Hinkley",
      "5ISL",   "", // CAD Twp
      "5KR",    "Kerrick",
      "5MCG",   "Mcgrath",
      "5MIK",   "",           // Error from Tib???
      "5MK",    "Markville",
      "5MOR",   "Mora",       // Kanabec County
      "5MSL",   "Moose Lake", // Carlton County
      "5NIK",   "Nickerson",
      "5PC",    "Pine City",
      "5RC",    "Rock Creek",
      "5RSC",   "Rush City",  // Chisago County
      "5RT",    "Rutledge",
      "5SL",    "Sturgeon Lake",
      "5SS",    "Sand Stone",
      "5WR",    "Willow River",

      // Carlton County
      "6ATT",   "Atkinson Twp",
      "6AUT",   "Automba Twp",
      "6BAR",   "Barnum",
      "6BHT",   "Blackhoof Twp",
      "6BRT",   "Barnum Twp",
      "6BST",   "Beseman Twp",
      "6CAR",   "Carlton",
      "6CCT",   "Clear Creek",
      "6CLQ",   "Cloquet",
      "6CRM",   "Cromwell",
      "6CRT",   "Corona",
      "6CSO",   "Carlton County",
      "6EGT",   "Eagle Twp",
      "6FD",    "Fond Du Lac Carlton County",
      "6FXC",   "",               // FX Community ??
      "6HLT",   "Holyoke Twp",
      "6KET",   "Kettle River",
      "6KVT",   "Kalevala Twp",
      "6LVT",   "Lakeview Twp",
      "6MAH",   "Mahtowa",
      "6MAT",   "Mahtowa Twp",
      "6MLT",   "Moose Lake Twp",
      "6MOO",   "Moose Lake",
      "6OOJ",   "",               // Outside 911 Community
      "6PLT",   "Perch Lake Twp",
      "6PRT",   "Progress Twp",     // ????
      "6RCT",   "Red Clover Twp",   // ???
      "6SBT",   "Silver Brook Twp",
      "6SCA",   "Scanlon",
      "6SKT",   "Skelton Twp",
      "6SLT",   "Silver Twp",
      "6SRT",   "Split Rock Twp",
      "6SWT",   "Sawyer",
      "6THO",   "Thomson",
      "6TL",    "Twin Lakes",
      "6TLT",   "Twin Lakes Twp",
      "6TMT",   "Thomson Twp",
      "6TPT",   "Thomson Twp",
      "6WRI",   "Wright",
      "6WRN",   "Wrenshall",
      "6WRT",   "Wrenshall Twp",

      // St Louis County
      "707",    "UNORG T55-R14",
      "708",    "UNORG 55-15",
      "709",    "UNORG 55-21",
      "710",    "UNORG 56-14",
      "711",    "UNORG 56-16",
      "712",    "UNORG 56-17",
      "713",    "UNORG 57-14",
      "714",    "UNORG 57-16",
      "715",    "",             // New Cad Twp
      "716",    "UNORG 59-16",
      "717",    "UNORG 59/60-18",
      "718",    "UNORG 59-21",
      "719",    "UNORG 60-18",
      "720",    "UNORG 60-19",
      "721",    "UNORG 60-20",
      "722",    "UNORG 61-12",
      "723",    "UNORG 61-13",
      "724",    "UNORG 61-14",
      "725",    "UNORG 61-17",
      "726",    "Unorg",
      "727",    "UNORG 62-17",
      "728",    "Unorg 62-21",
      "729",    "Unorg 63-14",
      "730",    "Unorg 63-15",
      "731",    "Unorg 63-17",
      "732",    "Unorg 63-19",
      "733",    "Unorg 63-21",
      "734",    "Unorg 64-12",
      "735",    "Unorg 64-13",
      "736",    "Unorg 64-14",
      "737",    "Unorg 64-15",
      "738",    "Unorg 64-16",
      "739",    "Unorg 64-17",
      "740",    "Unorg 64-21",
      "741",    "Unorg 65-12",
      "742",    "Unorg 65-13",
      "743",    "Unorg 65-13",
      "744",    "Unorg 65-15",
      "745",    "Unorg 65-16",
      "746",    "Unorg 65-21",
      "747",    "Unorg 66-12",
      "748",    "Unorg 66-13",
      "749",    "Unorg 66-14",
      "750",    "Unorg 66-15",
      "751",    "Unorg 66-16",
      "753",    "Unorg 66-20",
      "754",    "Unorg 66-21",
      "755",    "Unorg 67-13",
      "756",    "Unorg 67-14",
      "757",    "Unorg 67-15",
      "758",    "Unorg 67-17",
      "759",    "Unorg 67-18",
      "760",    "Unorg 67-19",
      "761",    "Unorg 67-20",
      "762",    "Unorg 67-21",
      "763",    "Unorg 68-17",
      "764",    "Unorg 68-18",
      "765",    "Unorg 68-19",
      "766",    "Unorg 68-20",
      "767",    "Unorg 68-21",
      "768",    "Unorg 69-21",
      "790",    "",            // New Cad Twp
      "7AG",    "Alango Twp",
      "7AN",    "Angora Twp",
      "7AU",    "Aurora",
      "7AV",    "",            // New Cad Twshp
      "7BA",    "Babbitt",
      "7BB",    "Babbitt Twp",
      "7BE",    "Beatty Twp",
      "7BI",    "Biwabik",
      "7BK",    "Balkan Twp",
      "7BS",    "Bassett Twp",
      "7BT",    "Breitung Twp",
      "7BU",    "Buhl",
      "7BW",    "Biwabik Twp",
      "7C5",    "Camp 5 Twp",
      "7CH",    "Chisholm",
      "7CK",    "Crane Lake",
      "7CL",    "Clinton Twp",
      "7CM",    "",  // New Cad Twp
      "7CN",    "Colvin Twp",
      "7CO",    "Cook",
      "7CR",    "Cherry Twp",
      "7EB",    "Ellsburg Twp",
      "7EL",    "Ely",
      "7EM",    "Embarrass Twp",
      "7EN",    "Eagles Nest Twp",
      "7EV",    "Eveleth",
      "7FA",    "Fayal Twp",
      "7FB",    "Fairbanks Twp",
      "7FH",    "French Twp",
      "7FI",    "Field Twp",
      "7GI",    "Gilbert",
      "7GS",    "Great Scott Twp",
      "7GW",    "Greenwood Twp",
      "7HI",    "Hibbing",
      "7HO",    "Hoyt Lakes",
      "7IR",    "Iron Junction",
      "7IT",    "Itasca County",
      "7KB",    "Kabetogama",
      "7KE",    "Kelsey Twp",
      "7KI",    "Kinney",
      "7KO",    "Koochiching County",
      "7KU",    "Kugler Twp",
      "7KV",    "",   // New Cad Twp
      "7LD",    "Leiding Twp",
      "7LE",    "Leonidas",
      "7LG",    "Linden Grove Twp",
      "7LV",    "Lavell Twp",
      "7MC",    "Mckinley",
      "7MD",    "Mcdavitt Twp",
      "7MO",    "Morse Twp",
      "7MR",    "Morcom Twp",
      "7MT",    "Mt Iron",
      "7OR",    "Orr",
      "7OW",    "Owens Twp",
      "7PI",    "Pike Twp",
      "7PO",    "Portage Twp",
      "7SA",    "Sandy Twp",
      "7SO",    "Soudan",
      "7ST",    "Sturgeon Twp",
      "7TO",    "Tower",
      "7VI",    "Virginia",
      "7VL",    "Vermilion Lake Twp",
      "7VP",    "Voyageurs",
      "7WA",    "Waasa Twp",
      "7WH",    "White Twp",
      "7WI",    "Winton",
      "7WR",    "Wuori Twp",
      "7WV",    "Willow Valley Twp",
      "AI",     "Aitkin County",
      "BF",     "Boise Ft",
      "BL",     "Bearville",
      "BM",     "Brimson",
      "BY",     "Buyck",
      "CC",     "Clear Creek Twp",
      "CE",     "Central Lakes",
      "CF",     "Clifton",
      "CLRBRK", "Clear Brook Twp",
      "DO",     "Douglas County",
      "EG",     "Evergreen",
      "ES",     "Esko",
      "FR",     "Franklin",
      "GR",     "Greaney",
      "GUNF",   "Gunflint/Loon Lakes",
      "HL",     "Hoyt Lakes Twp",
      "JA",     "Jacobson Map",
      "LL",     "Lakeland",
      "MA",     "Makinen",
      "ML",     "Moose Lake Twp",
      "OT",     "", // Other
      "P1",     "Palo",
      "PL",     "Perch Lake Twp",
      "SI",     "Silica",
      "SL",     "St Louis County",
      "SU",     "Superior",
      "XX",     "Highways South Psap", // ??
      "YY",     "Highways North Psap", // ??

  });

}
