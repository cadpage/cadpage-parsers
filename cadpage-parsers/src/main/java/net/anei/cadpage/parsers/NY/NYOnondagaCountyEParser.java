package net.anei.cadpage.parsers.NY;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;


public class NYOnondagaCountyEParser extends DispatchProQAParser {
  
  public NYOnondagaCountyEParser() {
    super("ONONDAGA COUNTY", "NY",
           "CALL1 ADDR APT? CALL2! INFO/N+? ID! END");
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL1")) return new MyCall1Field();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CALL2")) return new MyCall2Field();
    return super.getField(name);
  }
  
  private static final Pattern CALL1_PTN = Pattern.compile("(P\\d+[A-Z]?) - +(.*)");
  private class MyCall1Field extends Field {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = CALL1_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strPriority = match.group(1);
      data.strCall = match.group(2);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
    
    @Override
    public String getFieldNames() {
      return "PRI CALL";
    }
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      String line = ADDRESS_TABLE.getProperty(field);
      if (line != null) {
        String[] parts = line.split("/");
        data.strPlace = field;
        data.strAddress = parts[0];
        data.strCity = parts[1]; 
      } else {
        parseAddress(field, data);
        data.strCity = "SYRACUSE";
      }
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE ADDR APT CITY";
    }
  }
  
  private static final Pattern CALL2_PTN = Pattern.compile("(\\d{1,2}[A-Z]\\d{1,2}[A-Z]?)[- ]+(.*)");
  private class MyCall2Field extends Field {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.equals("<None>")) return true;
      Matcher match = CALL2_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strCode = match.group(1);
      data.strCall = append(data.strCall, " - ", match.group(2));
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
  
  @Override
  public String adjustMapAddress(String address) {
    int pt = address.indexOf(" - ");
    if (pt >= 0) address = address.substring(0,pt).trim();
    return super.adjustMapAddress(address);
  }
  
  private static final Properties ADDRESS_TABLE = buildCodeTable(new String[]{

      "1 CROUSE MEDICAL PLAZA",                                   "310 S CROUSE AVE/SYRACUSE",
      "60 PRESIDENTIAL PLAZA /MADISON TOWERS",                    "700 S TOWNSEND ST/SYRACUSE",
      "80 PRESIDENTIAL PLAZA",                                    "515 HARRISON ST/SYRACUSE",
      "90 PRESIDENTIAL PLAZA",                                    "521 HARRISON ST/SYRACUSE",
      "ACACIA FRATERNITY",                                        "781 OSTROM AVE/SYRACUSE",
      "ACROPOLIS PIZZA",                                          "167 MARSHALL ST/SYRACUSE",
      "ADAMS STREET PARKING GARAGE",                              "1302 E ADAMS ST/SYRACUSE",
      "ALIBRANDI CENTER",                                         "110 WALNUT PL/SYRACUSE",
      "ALPHA CHI RHO",                                            "131 COLLEGE PL/SYRACUSE",
      "ALPHA EPSILON PHI SORORITY",                               "751 COMSTOCK AVE/SYRACUSE",
      "ALPHA EPSILON PI FRATERNITY",                              "208 WALNUT PL/SYRACUSE",
      "ALPHA GAMMA DELTA SORORITY",                               "709 COMSTOCK AVE/SYRACUSE",
      "ALPHA PHI SORORITY",                                       "308 WALNUT PL/SYRACUSE",
      "ALPHA TAU OMEGA FRATERNITY",                               "304 WALNUT PL/SYRACUSE",
      "ALPHA XI DELTA SORORITY",                                  "125 EUCLID AVE/SYRACUSE",
      "ALS TRAINING CENTER",                                      "550 E GENESEE ST/SYRACUSE",
      "ALTO CINCO",                                               "526 WESTCOTT ST/SYRACUSE",
      "AMBASSADOR APTS",                                          "901 MADISON ST/SYRACUSE",
      "AMERICAN RED CROSS",                                       "636 S WARREN ST/SYRACUSE",
      "APPETHAIZING",                                             "727 S CROUSE AVE/SYRACUSE",
      "ARCHBOLD ACADEMIC",                                        "150 SIMS DR/SYRACUSE",
      "ARCHBOLD GYMNASIUM",                                       "150 SIMS DR/SYRACUSE",
      "ARCHBOLD THEATER AT SYRACUSE STAGE",                       "820 E GENESEE ST/SYRACUSE",
      "AUBURN COMMUNITY HOSPITAL",                                "17 LANSING ST/AUBURN",
      "BAKER LABORATORIES - ESF",                                 "210 CAMPUS DR W/SYRACUSE",
      "BARRY PARK",                                               "700 BROAD ST/SYRACUSE",
      "BEER BELLY DELI",                                          "510 WESTCOTT ST/SYRACUSE",
      "BELFER AUDIO ARCHIVES",                                    "222 WAVERLY AVE/SYRACUSE",
      "BERNICE WRIGHT NURSERY SCHOOL",                            "441 LAMBRETH LN/SYRACUSE",
      "BIOLOGY RESEARCH LAB (BRL)",                               "100 SIMS DR/SYRACUSE",
      "BLEU MONKEY CAFE",                                         "163 MARSHALL ST/SYRACUSE",
      "BOATHOUSE - ONONDAGA LAKE",                                "1 TEN EYCK DR/LIVERPOOL",
      "BOLAND HALL",                                              "401 VAN BUREN ST/SYRACUSE",
      "BOOTH HALL",                                               "505 COMSTOCK AVE/SYRACUSE",
      "BOOTH PARKING GARAGE",                                     "400 MARSHALL ST/SYRACUSE",
      "BOWNE HALL",                                               "130 SIMS DR/SYRACUSE",
      "BRAY HALL - ESF",                                          "131 CAMPUS DR E/SYRACUSE",
      "BREWSTER HALL",                                            "401 VAN BUREN ST/SYRACUSE",
      "BREWSTER PARKING GARAGE",                                  "401 VAN BUREN ST/SYRACUSE",
      "BRIGHTON HILL CHIROPRACTIC",                               "170 INTREPID LN/SYRACUSE",
      "BROCKWAY DINING CENTER",                                   "401 VAN BUREN ST/SYRACUSE",
      "BROCKWAY HALL",                                            "401 VAN BUREN ST/SYRACUSE",
      "BRUEGGERS BAGELS",                                         "731 S CROUSE AVE/SYRACUSE",
      "BURNETT PARK -CITY OF SYRACUSE",                           "299 COLERIDGE AVE/SYRACUSE",
      "BUTTERFIELD HOUSE",                                        "709 COMSTOCK AVE/SYRACUSE",
      "CALIOS",                                                  "123 MARHSALL ST/SYRACUSE",
      "CAMPUS PLAZA",                                             "727 S CROUSE AVE/SYRACUSE",
      "CAMPUS WEST APTS",                                         "150 HENRY ST/SYARCUSE",
      "CARMELO ANTHONY/CENTER",                                   "1301 E COLVIN ST/SYRACUSE",
      "CARNEGIE HALL",                                            "130 SIMS DR/SYRACUSE",
      "CARRIAGE HOUSE",                                           "161 FARM ACRE RD/SYRACUSE",
      "CARRIER DOME",                                             "901 IRVING AVE/SYRACUSE",
      "CENTENNIAL HALL",                                          "142 OAKLAND ST/SYRACUSE",
      "CENTER OF EXCELLENCE",                                     "727 E WASHINGTON ST/SYRACUSE",
      "CENTRAL NEW YORK EMS",                                     "50 PRESIDENTIAL PLZ/SYRACUSE",
      "CHABAD HOUSE",                                             "825 OSTROM AVE/SYRACUSE",
      "CHAMPIONSHIP PT",                                          "314 E FIRST ST/EAST SYRACUSE",
      "CHANCELLOR'S RESIDENCE",                                   "300 COMSTOCK AVE/SYRACUSE",
      "CHASE BANK",                                               "649 S CROUSE AVE/SYRACUSE",
      "CHAUFFEUR'S RESIDENCE",                                    "302 COMSTOCK AVE/SYRACUSE",
      "CHIPOTLE MEXICAN RESTAURANT",                              "131 MARSHALL ST/SYRACUSE",
      "CIM PHYSICIANS OFFICE BUILDING",                           "725 IRVING AVE/SYRACUSE",
      "CLAY MEDICAL CENTER",                                      "8100 OSWEGO RD/CLAY",
      "CLINTON SQUARE",                                           "101 W GENESEE ST/SYRACUSE",
      "CNY/ENT CONSULTANTS",                                      "1100 E GENESSE ST/SYRACUSE",
      "CNY/FAMILY CARE",                                          "4939 BRITTONFIELD PKWY/EAST SYRACUSE",
      "CNY/INTERNISTS",                                           "5823 WIDEWATERS PKWY/DEWITT",
      "CNY/MEDICAL CENTER",                                       "739 IRVING AVE/SYRACUSE",
      "CNY/SPORTS THERAPY",                                       "4713 ONONDAGA BLVD/GEDDES",
      "CNY/SURGICAL ASSOCIATES",                                  "739 IRVING AVE/SYRACUSE",
      "COLLEGE PLACE BUS STOP",                                   "126 COLLEGE PL/SYRACUSE",
      "COMFORT TYLER PARK",                                       "1100 COMSTOCK AVE/SYRACUSE",
      "COMMISSARY",                                               "201 AINSLEY DR/SYRACUSE",
      "COMMUNITY GENERAL HOSPITAL",                               "4900 BROAD RD/ONONDAGA",
      "COMSTOCK ART BUILDING",                                    "1075 COMSTOCK AVE/SYRACUSE",
      "COPPER BEECH COMMONS",                                     "300 UNIVERSITY AVE/SYRACUSE",
      "COUNSELING CENTER",                                        "200 WALNUT PL/SYRACUSE",
      "COYNE FIELD",                                              "1311 E COLVIN ST/SYRACUSE",
      "CPEP - ST JOSEPH'S HOSPITAL",                              "301 PROSPECT AVE/SYRACUSE",
      "CROUSE COLLEGE",                                           "100 CROUSE DR/SYRACUSE",
      "CROUSE HINDS HALL",                                        "900 S CROUSE AVE/SYRACUSE",
      "CROUSE HOSPITAL",                                          "736 IRVING AVE/SYRACUSE",
      "CROWNE PLAZA SYRACUSE",                                    "701 E GENESEE ST/SYRACUSE",
      "CVS PHARMACY",                                             "700 S CROUSE AVE/SYRACUSE",
      "DAILY ORANGE",                                             "744 OSTROM AVE/SYRACUSE",
      "DAISY DUKES",                                              "207 WALTON ST/SYRACUSE",
      "DANZERS",                                                  "153 AINSLEY DR/SYRACUSE",
      "DAY CARE CENTER (M-1)",                                    "421 LAMBRETH LN/SYRACUSE",
      "DAY HALL",                                                 "300 MOUNT OLYMPUS DR/SYRACUSE",
      "DELLPLAIN HALL",                                           "601 COMSTOCK AVE/SYRACUSE",
      "DELTA CHI",                                                "739 OSTROM AVE/SYRACUSE",
      "DELTA DELTA DELTA SORORITY",                               "300 WALNUT PL/SYRACUSE",
      "DELTA GAMMA SORORITY",                                     "901 WALNUT AVE/SYRACUSE",
      "DELTA KAPPA EPSILON FRATERNITY",                           "703 WALNUT AVE/SYRACUSE",
      "DELTA PHI EPSILON SORORITY",                               "705 WALNUT AVE/SYRACUSE",
      "DELTA TAU DELTA FRATERNITY",                               "801 WALNUT AVE/SYRACUSE",
      "DEPARTMENT OF PUBLIC SAFETY",                              "130 COLLEGE PL/SYRACUSE",
      "DESTINY/USA",                                              "1 DESTINY/USA DR/SYRACUSE",
      "DINEEN HALL",                                              "950 IRVING AVE/SYRACUSE",
      "DRUMLINS COUNTRY CLUB",                                    "800 NOTTINGHAM RD/DEWITT",
      "DRUMLINS RESTAURANT",                                      "800 NOTTINGHAM RD/SYRACUSE",
      "DRUMLINS WEST GOLF COURSE",                                "800 NOTTINGHAM RD/SYRACUSE",
      "DUNKIN DONUTS",                                            "746 S CROUSE AVE/SYRACUSE",
      "E GENESEE MEDICAL",                                        "1200 E GENESEE ST/SYRACUSE",
      "E.S. BIRD LIBRARY",                                        "222 WAVERLY AVE/SYRACUSE",
      "EAVES AMBULANCE",                                          "6440 NEW VENTURE GEAR DR/EAST SYRACUSE",
      "EGGERS HALL",                                              "120 CROUSE DR/SYRACUSE",
      "Endodontic Associates",                                    "600 E GENESSE ST/SYRACUSE",
      "ENSLEY ATHLETIC CENTER",                                   "1315 E COLVIN ST/SYRACUSE",
      "ENT ASSOCIATES",                                           "406 UNIVERSITY AVE/SYRACUSE",
      "ERNIE DAVIS HALL",                                         "619 COMSTOCK AVE/SYRACUSE",
      "ESF GENERIC CAMPUS ADDRESS",                               "1 FORESTRY DR/SYRACUSE",
      "ESF GROUNDS BLDG",                                         "150 CAMPUS DR E/SYRACUSE",
      "ESF OLD GREENHOUSE",                                       "160 CAMPUS DR E/SYRACUSE",
      "ESF PHYSICAL PLANT",                                       "307 STADIUM PL/SYRACUSE",
      "EXSCAPE",                                                  "167 MARSHALL ST/SYRACUSE",
      "EYE ASSOCIATES 2",                                         "2215 E GENESEE ST/SYRACUSE",
      "EYE ASSOCIATES",                                           "514 OAK ST/SYRACUSE",
      "EYE CONSULTANTS",                                          "1101 ERIE BLVD E/SYRACUSE",
      "FAEGANS PUB",                                              "734 S CROUSE AVE/SYRACUSE",
      "FALK COLLEGE",                                             "150 CROUSE DR/SYRACUSE",
      "FINE P-LOT",                                               "101 STANDART ST/SYRACUSE",
      "FLANAGAN GYMNASIUM",                                       "151 SIMS DR/SYRACUSE",
      "FLINT HALL",                                               "100 MOUNT OLYMPUS DR/SYRACUSE",
      "FOOT SPECIALISTS OF CNY/",                                 "315 S CROUSE AVE/SYRACUSE",
      "FRANCOS PIZZERIA",                                         "901 E GENESEE ST/SYRACUSE",
      "FRANKLIN SQUARE ORTHODONTICS",                             "526 PLUM ST/SYRACUSE",
      "FUNK N WAFFLES",                                           "727 S CROUSE AVE/SYRACUSE",
      "GAMMA PHI BETA SORORITY",                                  "803 WALNUT AVE/SYRACUSE",
      "GANNONS ISLE",                                             "1525 VALLEY DR/SYRACUSE",
      "GATEWAY CENTER - ESF",                                     "106 CAMPUS DR/SYRACUSE",
      "GENESEE GRAND HOTEL",                                      "1060 E GENESEE ST/SYRACUSE",
      "GOLDSTEIN ALUMNI & FACULTY CENTER",                        "401 UNIVERSITY PL/SYRACUSE",
      "GOLDSTEIN AUDITORIUM - SCHINE STUDENT CENTER",             "301 UNIVERSITY PL/SYRACUSE",
      "GOLDSTEIN STUDENT CENTER",                                 "401 SKYTOP RD/SYRACUSE",
      "GRAHAM DINING CENTER",                                     "200 MOUNT OLYMPUS DR/SYRACUSE",
      "GRANGE BUILDING",                                          "101 S WARREN ST/SYRACUSE",
      "GRANT AUDITORIUM - GRANT HALL",                            "150 CROUSE DR/SYRACUSE",
      "GREATER BALDWINSVILLE AMBULANCE CORPS",                    "11 ALBERT PALMER LN/BALDWINSVILLE",
      "GREEN DATA CENTER",                                        "623 SKYTOP RD/ONONDAGA",
      "HAFT HALL",                                                "795 OSTROM AVE/SYRACUSE",
      "HAIR TRENDS",                                              "727 S CROUSE AVE/SYRACUSE",
      "HALL OF LANGUAGES",                                        "101 CROUSE DR/SYRACUSE",
      "HALO TATTOO",                                              "171 MARSHALL ST/SYRACUSE",
      "HANCOCK INTERNATIONAL AIRPORT",                            "1000 COL EILEEN COLLINS BLVD/NORTH SYRACUSE",
      "HARRISON CENTER",                                          "550 HARRISON ST/SYRACUSE",
      "HARRISON P-LOT",                                           "510 UNIVERSITY AVE/SYRACUSE",
      "HARRYS BAR",                                               "700 S CROUSE AVE/SYRACUSE",
      "HAVEN HALL",                                               "400 COMSTOCK AVE/SYRACUSE",
      "HAWKINS WAREHOUSE",                                        "1600 JAMESVILLE AVE/ONONDAGA",
      "HEALING POINT ACUPUNCTURE",                                "6868 E GENESEE ST/DEWITT",
      "HEALTH CENTER EAST",                                       "2803 ERIE BLVD E/SYRACUSE",
      "HENDRICKS CHAPEL",                                         "121 CROUSE DR/SYRACUSE",
      "HENDRICKS FIELD",                                          "1000 IRVING AVE/SYRACUSE",
      "HEROY GEOLOGY BUILDING",                                   "141 CROUSE DR/SYRACUSE",
      "HILL MEDICAL CENTER",                                      "1000 E GENESEE ST/SYRACUSE",
      "HILLEL CENTER",                                            "102 WALNUT PL/SYRACUSE",
      "HILLSIDE P-LOT",                                           "100 UNIVERSITY PL/SYRACUSE",
      "HINDS HALL",                                               "110 SMITH DR/SYRACUSE",
      "HOLDEN OBSERVATORY",                                       "140 CROUSE DR/SYRACUSE",
      "HOOKWAY FIELD COMPLEX",                                    "1801-1901 E COLVIN ST/SYRACUSE",
      "HOOPLE SPECIAL EDUCATION BUILDING",                        "805 S CROUSE AVE/SYRACUSE",
      "HOTEL SKYLER",                                             "601 S CROUSE AVE/SYRACUSE",
      "HUNTINGTON BEARD CROUSE",                                  "111 CROUSE DR/SYRACUSE",
      "HUNTINGTON FAMILY CENTER",                                 "405 GIFFORD ST/SYRACUSE",
      "HUNTINGTON HALL",                                          "804 UNIVERSITY AVE/SYRACUSE",
      "HUTCHINGS PSYCHIATRIC CENTER",                             "713 HARRISON ST/SYRACUSE",
      "ILLICK HALL - ESF",                                        "101 FORESTRY DR/SYRACUSE",
      "INDUSTRIAL MEDICAL ASSOCIATES",                            "961 CANAL ST/SYRACUSE",
      "INN COMPLETE - SKI LODGE",                                 "610 SKYTOP RD/ONONDAGA",
      "INSOMNIA COOKIES",                                         "137 MARSHALL ST/SYRACUSE",
      "INSTITUTE FOR HUMAN PERFORMANCE",                          "505 IRVING AVE/SYRACUSE",
      "INSTITUTE SENSORY RESEARCH",                               "621 SKYTOP RD/SYRACUSE",
      "IRVING PARKING GARAGE",                                    "918 IRVING AVE/SYRACUSE",
      "ISLAMIC SOCIETY MOSQUE",                                   "925 COMSTOCK AVE/SYRACUSE",
      "J MICHAEL SHOES",                                          "173 MARSHALL ST/SYRACUSE",
      "JAHN LAB - ESF",                                           "200 CAMPUS DR W/SYRACUSE",
      "JIMMY JOHNS",                                              "103 MARSHALL ST/SYRACUSE",
      "JOSLIN DIABETES CENTER",                                   "3229 E GENESEE ST/SYRACUSE",
      "KAPPA ALPHA THETA SORORITY",                               "306 WALNUT PL/SYRACUSE",
      "KAPPA DELTA RHO FRATERNITY",                               "795 OSTROM AVE/SYRACUSE",
      "KAPPA KAPPA GAMMA SORORITY",                               "743 COMSTOCK AVE/SYRACUSE",
      "KIMMEL FOOD COURT",                                        "503 COMSTOCK AVE/SYRACUSE",
      "KIMMEL HALL",                                              "311 WAVERLY AVE/SYRACUSE",
      "KING DAVIDS RESTAURANT",                                   "129 MARSHALL ST/SYRACUSE",
      "KOERNER FORD",                                             "805 W GENESEE ST/SYRACUSE",
      "LANCASTER MARKET",                                         "1007 LANCASTER AVE/SYRACUSE",
      "LANDMARK AVIATION",                                        "248 TUSKEGEE RD/SALINA",
      "LANDMARK THEATER",                                         "362 S SALINA ST/SYRACUSE",
      "LAW SCHOOL",                                               "950 IRVING AVE/SYRACUSE",
      "LAWRINSON HALL",                                           "303 STADIUM PL/SYRACUSE",
      "LEGAL SERVICES",                                           "760 OSTROM AVE/SYRACUSE",
      "LEHMAN P-LOT",                                             "606 UNIVERSITY AVE/SYRACUSE",
      "LGBT RESOURCE CENTER",                                     "750 OSTROM AVE/SYRACUSE",
      "LIFE SCIENCES BUILDING",                                   "107 COLLEGE PL/SYRACUSE",
      "LIMESTONE PROFESSIONAL BLD",                               "6838 E GENESEE ST/DEWITT",
      "LINK HALL",                                                "130 SMITH DR/SYRACUSE",
      "LONGO",                                                    " MD,739 IRVING AVE/SYRACUSE",
      "LOWE ART GALLERY - SIMS HALL",                             "130 COLLEGE PL/SYRACUSE",
      "LOWER HOOKWAY FIELD",                                      "1801 E COLVIN ST/SYRACUSE",
      "LYMAN HALL",                                               "100 COLLEGE PL/SYRACUSE",
      "LYONS HALL",                                               "401 EUCLID AVE/SYRACUSE",
      "M-STREET PIZZA",                                           "113 MARSHALL ST/SYRACUSE",
      "MACHINERY HALL",                                           "120 SMITH DR/SYRACUSE",
      "MACNAUGHTON HALL",                                         "150 CROUSE DR/SYRACUSE",
      "MADISON-IRVING MEDICAL CENTER",                            "475 IRVING AVE/SYRACUSE",
      "MAIN QUAD",                                                "CROUSE DR/SYRACUSE",
      "MANLEY FIELD HOUSE",                                       "1301 E COLVIN ST/SYRACUSE",
      "MANLEY NORTH P-LOT",                                       "COMSTOCK AVE/SYRACUSE",
      "MANLEY SOUTH P-LOT",                                       "E COLVIN ST/SYRACUSE",
      "MANNY/S",                                                  "151 MARSHALL ST/SYRACUSE",
      "MARION HALL",                                              "305 WAVERLY AVE/SYRACUSE",
      "MARLEY EDUCATION CENTER",                                  "765 IRVING AVE/SYRACUSE",
      "MARSHALL HALL - ESF",                                      "121 CAMPUS DRIVE E/SYRACUSE",
      "MARSHALL SQUARE MALL",                                     "720 UNIVERSITY AVE/SYRACUSE",
      "MAXWELL HALL",                                             "110 CROUSE DR/SYRACUSE",
      "MCCARTHY MANOR",                                           "501 S CROUSE AVE/SYRACUSE",
      "MEDICAL CENTER EAST",                                      "5900 N BURDICK ST/EAST SYRACUSE",
      "MEDICAL CENTER WEST",                                      "5700 W GENESEE ST/CAMILLUS",
      "MELLO VELLO BICYCLE SHOP",                                 "556 WESTCOTT ST/SYRACUSE",
      "MOMS DINER",                                               "501 WESTCOTT ST/SYRACUSE",
      "MOON LIBRARY - ESF",                                       "111 CAMPUS DR/SYRACUSE",
      "MORNINGSIDE HEIGHTS PARK",                                 "100 BROAD ST/SYRACUSE",
      "MORNINGSIDE MAUSOLEUM",                                    "1001 COMSTOCK AVE/SYRACUSE",
      "NBT BANK OF SYRACUSE",                                     "101 MARSHALL ST/SYRACUSE",
      "NEWHOUSE COMPLEX",                                         "215 UNIVERSITY PL/SYRACUSE",
      "NEWHOUSE I",                                               "215 UNIVERSITY PL/SYRACUSE",
      "NEWHOUSE II",                                              "215 UNIVERSITY PL/SYRACUSE",
      "NEWHOUSE III",                                             "215 UNIVERSITY PL/SYRACUSE",
      "NICKS TOMATO PIE",                                         "109 WALTON ST/SYRACUSE",
      "NOB HILL APTS BLDG 1",                                     "111 LAFAYETTE RD/SYRACUSE",
      "NOB HILL APTS BLDG 2",                                     "121 LAFAYETTE RD/SYRACUSE",
      "NOB HILL APTS BLDG 3",                                     "211 LAFAYETTE RD/SYRACUSE",
      "NOB HILL APTS BLDG 4",                                     "241 LAFAYETTE RD/SYRACUSE",
      "NORTH AREA VOLUNTEER AMBULANCE CORPS.",                    "603 N MAIN ST/NORTH SYRACUSE",
      "NORTH MEDICAL CENTER",                                     "5100 W TAFT RD/CLAY",
      "NORTHEAST MEDICAL CENTER",                                 "4117 MEDICAL CENTER DR/FAYETTEVILLE",
      "NORTHEASTERN RESCUE VEHICLES",                             "6764 PICKARD DR/DEWITT",
      "NOSE AND THROAT CONSULTANTS",                              "1100 E GENESEE ST/SYRACUSE",
      "NOTTINGHAM HIGH SCHOOL",                                   "3100 E GENESSE ST/SYRACUSE",
      "NOTTINGHAM PLAZA",                                         "311 NOTTINGHAM RD/SYRACUSE",
      "NURSING SCHOOL (Ecology Dept)",                            "426 OSTROM AVE/SYRACUSE",
      "OAKWOOD CEMETERY",                                         "940 COMSTOCK AVE/SYRACUSE",
      "OFF CAMPUS COMMUTER SERVICES",                             "754 OSTROM AVE/SYRACUSE",
      "ONCENTER",                                                 "800 S STATE ST/SYRACUSE",
      "ONONDAGA COUNTY 911",                                      "3911 CENTRAL AVE/ONONDAGA",
      "ONONDAGA COUNTY CIVIC CENTER",                             "421 MONTGOMERY ST/SYRACUSE",
      "ONONDAGA COUNTY HEALTH DEPT 2",                            "301 SLOCUM AVE/SYRACUSE",
      "ONONDAGA COUNTY HEALTH DEPT",                              "421 MONTGOMERY ST/SYRACUSE",
      "ORANGE GROVE 1",                                           "136 SIMS DR/SYRACUSE",
      "ORANGE GROVE 2",                                           "142 SIMS DR/SYRACUSE",
      "ORTHONOW",                                                 "6620 FLY RD/EAST SYRACUSE",
      "ORTHOPEDIC ASSOCIATES OF CNY/I",                           "475 IRVING AVE/SYRACUSE",
      "ORTHOPEDIC ASSOCIATES OF CNY/II",                          "5823 WIDEWATERS PKWY/DEWITT",
      "ORTHOPEDIC REHAB SERVICES",                                "3229 E GENESEE ST/SYARCUSE",
      "Orthopedics East",                                         "183 INTREPID LN/SYRACUSE",
      "OSTROM P-LOT",                                             "OSTROM AVE/SYRACUSE",
      "OUTDOOR EDUCATION CENTER & CHALLENGE COURSE",              "600 SKYTOP RD/ONONDAGA",
      "PANDA WEST CHINESE RESTAURANT",                            "135 MARSHALL ST/SYRACUSE",
      "PARK POINT APTS",                                          "417 COMSTOCK AVE/SYRACUSE",
      "PARKVIEW HOTEL",                                           "713 E GENESSEE ST/SYRACUSE",
      "PASTABILITIES",                                            "311 S FRANKLIN ST/SYRACUSE",
      "PHI BETA SIGMA FRATERNITY",                                "604 1/2 UNIVERSITY AVE/SYRACUSE",
      "PHI DELTA THETA",                                          "210 WALNUT PL/SYRACUSE",
      "PHI GAMMA DELTA FRATERNITY",                               "726 OSTROM AVE/SYRACUSE",
      "PHI KAPPA PSI FRATERNITY",                                 "500 UNIVERSITY PL/SYRACUSE",
      "PHI KAPPA THETA FRATERNITY",                               "777 OSTROM AVE/SYRACUSE",
      "PHI SIGMA SIGMA",                                          "1003 WALNUT AVE/SYRACUSE",
      "PHYSICAL PLANT",                                           "285 AINSLEY DR/SYRACUSE",
      "PHYSICAL THERAPY PLUS",                                    "207 PINE ST/SYRACUSE",
      "PHYSICS BUILDING",                                         "125 CROUSE DR/SYRACUSE",
      "PITA PIT",                                                 "107 MARSHALL ST/SYRACUSE",
      "PLANNED PARENTHOOD",                                       "1120 E GENESEE ST/SYRACUSE",
      "POLE BARN",                                                "620 SKYTOP RD/SYRACUSE",
      "PRESIDENTIAL PLAZA",                                       "600 E GENESEE ST/SYRACUSE",
      "PRINTING PLANT",                                           "216 BURNET AVE/SYRACUSE",
      "PROMPT CARE - CIM/CNY/",                                   "739 IRVING AVE/SYRACUSE",
      "PSI UPSILON FRATERNITY",                                   "101 COLLEGE PL/SYRACUSE",
      "PSYCHIATIC WELLNESS CENTER",                               "1816 ERIE BLVD E/SYRACUSE",
      "PUBLIC SAFETY BUILDING (CITY)",                            "511 S STATE ST/SYRACUSE",
      "QUAD 1 P-LOT",                                             "CROUSE DR/SYRACUSE",
      "QUAD 2 P-LOT",                                             "SMITH DR/SYRACUSE",
      "QUAD 3 P-LOT",                                             "SIMS DR/SYRACUSE",
      "QUAD 4 P-LOT",                                             "COLLEGE PL/SYRACUSE",
      "QUAD 5 P-LOT",                                             "UNIVERSITY PL/SYRACUSE",
      "R1 P-LOT",                                                 "WAVERLY AVE/SYRACUSE",
      "R12 P-LOT",                                                "COMSTOCK AVE/SYRACUSE",
      "R2 P-LOT",                                                 "WAVERLY AVE/SYRACUSE",
      "R3 P-LOT",                                                 "COMSTOCK AVE/SYRACUSE",
      "R4 P-LOT",                                                 "E COLVIN ST/SYRACUSE",
      "R5 P-LOT",                                                 "CROUSE DR/SYRACUSE",
      "R6 P-LOT",                                                 "WAVERLY AVE/SYRACUSE",
      "R7 P-LOT",                                                 "UNIVERSITY PL/SYRACUSE",
      "R8 P-LOT",                                                 "424 OSTROM AVE/SYRACUSE",
      "REGIONAL TRANSPORTATION CENTER",                           "1 WALSH CIR/SYRACUSE",
      "RESCUE MISSION - ALCOHOL REHAB",                           "120 GIFFORD ST/SYRACUSE",
      "RESCUE MISSION NEW LIFE CENTER",                           "155 GIFFORD ST/SYRACUSE",
      "RESEARCH ONCOLOGY",                                        "760 E ADAMS ST/SYRACUSE",
      "RITE-AID PHARMACY - NOTTINGHAM PLAZA",                     "315 NOTTINGHAM RD/DEWITT",
      "RITE-AID PHARMACY - SKYTOP PLAZA",                         "602 NOTTINGHAM RD/DEWITT",
      "RITE-AID PHARMACY",                                        "1405 E GENESEE ST/SYRACUSE",
      "ROOSEVELT APTS",                                           "1301 E GENESEE ST/SYRACUSE",
      "ROSEWOOD HEIGHTS",                                         "614 S CROUSE AVE/SYRACUSE",
      "ROTHSCHILD MEDICAL SUPPLY",                                "805 E GENESEE ST/SYRACUSE",
      "RURAL/METRO MEDICAL SERVICES",                             "488 W ONONDAGA ST/SYRACUSE",
      "SADLER DINING CENTER",                                     "1000 IRVING AVE/SYRACUSE",
      "SADLER HALL",                                              "1000 IRVING AVE/SYRACUSE",
      "SAVES AMBULANCE",                                          "77 FENNELL ST/SKANEATELES",
      "SCHINE STUDENT CENTER",                                    "301 UNIVERSITY PL/SYRACUSE",
      "SCHWARTZWALDER FIELD",                                     "1319 E COLVIN ST/SYRACUSE",
      "SCIENCE & TECH BUILDING",                                  "111 COLLEGE PL/SYRACUSE",
      "SEFCU",                                                    "721 S CROUSE AVE/SYRACUSE",
      "SFD STATION 1",                                            "900 S STATE ST/SYRACUSE",
      "SFD STATION 10",                                           "2030 E GENESEE ST/SYRACUSE",
      "SFD STATION 17",                                           "2317 BURNET AVE/SYRACUSE",
      "SFD STATION 18",                                           "3801 MIDLAND AVE/SYRACUSE",
      "SFD STATION 2",                                            "2300 LODI ST/SYRACUSE",
      "SFD STATION 3",                                            "808 BELLEVUE AVE/SYRACUSE",
      "SFD STATION 4 - AIRPORT",                                  "110 OBSERVATION CIR/DEWITT",
      "SFD STATION 5",                                            "110 N GEDDES ST/SYRACUSE",
      "SFD STATION 6",                                            "601 S WEST ST/SYRACUSE",
      "SFD STATION 8",                                            "2412 S SALINA ST/SYRACUSE",
      "SFD STATION 9",                                            "400 SHUART AVE/SYRACUSE",
      "SHAFFER ART BUILDING",                                     "110 SIMS DR/SYRACUSE",
      "SHAW HALL",                                                "201 EUCLID AVE/SYRACUSE",
      "SHERATON GARAGE",                                          "801 UNIVERSITY AVE/SYRACUSE",
      "SHERATON UNIVERSITY HOTEL",                                "801 UNIVERSITY AVE/SYRACUSE",
      "SHERBROOKE APTS",                                          "604 WALNUT AVE/SYRACUSE",
      "SHIRT WORLD",                                              "125 MARSHALL ST/SYRACUSE",
      "SIBLEY POOL - WOMEN'S BUILD.",                             "820 COMSTOCK AVE/SYRACUSE",
      "SIGMA ALPHA EPSILON FRATERNITY",                           "206 WALNUT PL/SYRACUSE",
      "SIGMA ALPHA MU FRATERNITY",                                "727 COMSTOCK AVE/SYRACUSE",
      "SIGMA CHI FRATERNITY",                                     "737 COMSTOCK AVE/SYRACUSE",
      "SIGMA DELTA TAU SORORITY",                                 "336 COMSTOCK AVE/SYRACUSE",
      "SIGMA PHI EPSILON FRATERNITY",                             "721 COMSTOCK AVE/SYRACUSE",
      "SIMS HALL",                                                "130 COLLEGE PL/SYRACUSE",
      "SKYBARN",                                                  "151 FARM ACRE RD/SYRACUSE",
      "SKYHALL 1",                                                "410 LAMBRETH LN/SYRACUSE",
      "SKYHALL 2",                                                "420 LAMBRETH LN/SYRACUSE",
      "SKYHALL 3",                                                "430 LAMBRETH LN/SYRACUSE",
      "SKYTOP FIELD",                                             "600 BLOCK SKYTOP RD/ONONDAGA",
      "SKYTOP OFFICE BUILDING",                                   "611 SKYTOP RD/ONONDAGA",
      "SKYTOP PLAZA",                                             "620 NOTTINGHAM RD/DEWITT",
      "SLOCUM HALL",                                              "120 COLLEGE PL/SYRACUSE",
      "SLUTZGER CENTER FOR INTERNATIONAL SERVICES",               "310 WALNUT PL/SYRACUSE",
      "SMITH HALL",                                               "100 SMITH DR/SYRACUSE",
      "SOCCER STADIUM",                                           "1329 E COLVIN ST/SYRACUSE",
      "SOFTBALL COMPLEX",                                         "451 LAMBRETH LN/SYRACUSE",
      "SOME GIRLS BOUTIQUE",                                      "145 MARSHALL ST/SYRACUSE",
      "SOS ONONDAGA HILL",                                        "5000 W SENECA TPKE/ONONDAGA",
      "SPD EASTSIDE",                                             "473 WESTCOTT ST/SYRACUSE",
      "ST JOSEPH'S HOSPITAL",                                     "301 PROSPECT AVE/SYRACUSE",
      "ST JOSEPHS PSYCHIATRIC SERVICE",                           "112 DEWITT ST/SYRACUSE",
      "ST. JOSEPH'S IMAGING",                                     "109 PINE ST/SYRACUSE",
      "STA TRAVEL",                                               "107 MARSHALL ST/SYRACUSE",
      "STADIUM P-LOT",                                            "102 STADIUM PL/SYRACUSE",
      "STAGE P-LOT",                                              "425 IRVING AVE/SYRACUSE",
      "STANDART P-LOT",                                           "130 STANDARD ST/SYRACUSE",
      "STARBUCKS COFFEE (CAMPUS WEST)",                           "150 HENRY ST/SYRACUSE",
      "STARBUCKS COFFEE (MARSHALL ST)",                           "177 MARSHALL ST/SYRACUSE",
      "STATION 2",                                                "856 LANCASTER AVE/SYRACUSE",
      "STEAM STATION",                                            "500 E TAYLOR ST/SYRACUSE",
      "STEELE HALL",                                              "131 CROUSE DR/SYRACUSE",
      "STUDENTS CHOICE",                                          "161 MARSHALL ST/SYRACUSE",
      "SU ABROAD",                                                "106 WALNUT PL/SYRACUSE",
      "SU AMBULANCE",                                             "111 WAVERLY AVE/SYRACUSE",
      "SU HEALTH SERVICES",                                       "111 WAVERLY AVE/SYRACUSE",
      "SU PRESS",                                                 "1600 JAMESVILLE AVE/SYRACUSE",
      "SUNY/ESF POLICE",                                          "131 ESF CAMPUS DR E/SYRACUSE",
      "SUNY/HSC PSYCH. BEHAVIORAL CTR",                           "713 HARRISON ST/SYRACUSE",
      "SUPPORTIVE THERAPY SERVICES",                              "6858 E GENESEE ST/DEWITT",
      "SYRACUSE CITY COURT",                                      "511 S STATE ST/SYRACUSE",
      "SYRACUSE COMM. HEALTH CENTER",                             "819 S SALINA ST/SYRACUSE",
      "SYRACUSE ENT SURGEONS",                                    "3906 E GENESEE ST/SYRACUSE",
      "SYRACUSE EYE CENTER",                                      "612 UNIVERSITY AVE/SYRACUSE",
      "SYRACUSE ONCOLOGY-HEMATOLOGY",                             "2200 E GENESEE ST/SYRACUSE",
      "SYRACUSE ORTHOPEDIC SPECIALISTS III",                      "4888 W TAFT RD/NORTH SYRACUSE",
      "SYRACUSE ORTHOPEDIC SPECIALISTS",                          "5719 WIDEWATERS PKWY/DEWITT",
      "SYRACUSE STAGE",                                           "820 E GENESEE ST/SYRACUSE",
      "TAU KAPPA EPSILON",                                        "747 COMSTOCK AVE/SYRACUSE",
      "TEMPLE SOCIETY",                                           "501 UNIVERSITY AVE/SYRACUSE",
      "TENNITY ICE SKATING PAVILLION",                            "511 SKYTOP RD/SYRACUSE",
      "THE MOON",                                                 "123 MILKY WAY/THE MOON",
      "THETA CHI FRATERNITY",                                     "711 COMSTOCK AVE/SYRACUSE",
      "THETA TAU FRATERNITY",                                     "1105 HARRISON ST/SYRACUSE",
      "THORNDEN AMPHITHEATER",                                    "501 OSTROM AVE/SYRACUSE",
      "THORNDEN PARK POOL",                                       "600 S BEECH ST/SYRACUSE",
      "THORNDEN PARK",                                            "201 OSTROM AVE/SYRACUSE",
      "THORNDEN ROSE GARDEN",                                     "701 OSTROM AVE/SYRACUSE",
      "TIM HORTONS",                                              "985 E BRIGHTON AVE/SYRACUSE",
      "TLC EEMERGENCY MEDICAL SERVICES",                          "638 BURNET AVE/SYRACUSE",
      "TOAD HALL",                                                "302 MARSHALL ST/SYRACUSE",
      "TOLLEY ADMINISTRATION BUILDING",                           "130 CROUSE DR/SYRACUSE",
      "TOOMEY ABBOTT TOWERS",                                     "1207 ALMOND ST/SYRACUSE",
      "TOPS FRIENDLY MARKETS",                                    "620 NOTTINGHAM RD/DEWITT",
      "UNITED RADIO",                                             "2949 ERIE BLVD E/SYRACUSE",
      "UNITED STATES FEDERAL BLDG",                               "100 S CLINTON ST/SYRACUSE",
      "UNITED UNIFORM",                                           "709 E GENESEE ST/SYRACUSE",
      "UNIVERSITY AVE PARKING GARAGE",                            "1101 E ADAMS ST/SYRACUSE",
      "UNIVERSITY CHIROPRACTIC",                                  "465 WESTCOTT ST/SYRACUSE",
      "UNIVERSITY COLLEGE",                                       "700 UNIVERSITY AVE/SYRACUSE",
      "UNIVERSITY COMMUNICATIONS",                                "820 COMSTOCK AVE/SYRACUSE",
      "UNIVERSITY GUEST HOUSE",                                   "509 UNIVERSITY AVE/SYRACUSE",
      "UNIVERSITY HOSPITAL PEDIATRICS",                           "750 E ADAMS ST/SYRACUSE",
      "UNIVERSITY HOSPITAL",                                      "750 E ADAMS ST/SYRACUSE",
      "UNIVERSITY ORTHOPEDICS",                                   "407 UNIVERSITY AVE/SYRACUSE",
      "UNIVERSITY P-LOT",                                         "UNIVERSITY AVE/SYRACUSE",
      "UNIVERSITY RESEARCH PARK",                                 "620 SKYTOP RD/ONONDAGA",
      "UPPER HOOKWAY FIELD",                                      "1901 E COLVIN ST/SYRACUSE",
      "UPSTATE BONE & JOINT/ORTHONOW",                            "6620 FLY RD/EAST SYRACUSE",
      "VA PARKING GARAGE",                                        "900 IRVING AVE/SYRACUSE",
      "VARSITY PIZZA",                                            "802 S CROUSE AVE/SYRACUSE",
      "VERA HOUSE",                                               "6181 THOMPSON RD/DEWITT",
      "VETERANS ADMIN. HOSPITAL",                                 "800 IRVING AVE/SYRACUSE",
      "VINCENT HOUSE",                                            "500 SEYMOUR ST/SYRACUSE",
      "WAER RADIO STATION - HAFT HALL",                           "795 OSTROM AVE/SYRACUSE",
      "WALNUT HALL",                                              "809 WALNUT AVE/SYRACUSE",
      "WALNUT PARK",                                              "700 WALNUT AVE/SYRACUSE",
      "WALTERS HALL - ESF",                                       "141 CAMPUS DR E/SYRACUSE",
      "WAREHOUSE",                                                "350 W FAYETTE ST/SYRACUSE",
      "WASHINGTON ARMS",                                          "621 WALNUT AVE/SYRACUSE",
      "WATSON HALL",                                              "405 UNIVERSITY PL/SYRACUSE",
      "WATSON THEATER - WATSON HALL",                             "405 UNIVERSITY PL/SYRACUSE",
      "WAVERLY P-LOT",                                            "801 S CROUSE AVE/SYRACUSE",
      "WAVES AMBULANCE",                                          "202 BENNETT RD/CAMILLUS",
      "WEBSTER POOL - ARCHBOLD GYM",                              "150 SIMS DR/SYRACUSE",
      "WEGMANS PHARMACY",                                         "6789 E GENESSE ST/DE WITT",
      "WEISKOTTEN HALL - SUNY/UPSTATE",                           "766 IRVING AVE/SYRACUSE",
      "WELCOME CENTER",                                           "160 SKYTOP RD/SYRACUSE",
      "WEST SIDE MEDICAL CENTER",                                 "216 SEYMOUR ST/SYRACUSE",
      "WEST TAFT MEDICAL PARK",                                   "4820 W TAFT RD/NORTH SYRACUSE",
      "WESTCOTT THEATRE",                                         "524 WESTCOTT ST/SYRACUSE",
      "WESTMINSTER PARK",                                         "100 WESTMINSTER AVE/SYRACUSE",
      "WHITE HALL",                                               "150 CROUSE DR/SYRACUSE",
      "WHITMAN SCHOOL OF MANAGEMENT",                             "721 UNIVERSITY AVE/SYRACUSE",
      "WOHL FIELD",                                               "1325 E COLVIN ST/SYRACUSE",
      "WOMENS BUILDING",                                          "820 COMSTOCK AVE/SYRACUSE",
      "YOGURTLAND",                                               "147 MARSHALL ST/SYRACUSE",
      "ZETA BETA TAU FRATERNITY",                                 "905 WALNUT AVE/SYRACUSE",

  });
}
