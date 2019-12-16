package net.anei.cadpage.parsers.MD;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class MDSaintMarysCountyParser extends SmartAddressParser {
  
  public MDSaintMarysCountyParser() {
    super("SAINT MARYS COUNTY", "MD");
    setFieldList("TIME CALL ADDR APT X PLACE CITY UNIT INFO");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
    setupProtectedNames("BARNES AND YEH");
  }
  
  @Override
  public String getFilter() {
    return "mplus@co.saint-marys.md.us,mplus@STMARYSMD.COM,777,888";
  }
  
  private static final Pattern MARKER = Pattern.compile("\\b\\d\\d:\\d\\d:\\d\\d\\*");
  private static final Pattern PLACE = Pattern.compile("\\*\\*([^*]+)\\*\\*");
  private static final Pattern INFO_BRK_PTN = Pattern.compile(" +(?=\\d{1,2}\\. )");

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MARKER.matcher(body);
    if (!match.find()) return false;
    body = body.substring(match.start()).trim().replace("\n", "");
    if (body.endsWith(" stop")) body = body.substring(0,body.length()-5).trim();
    
    // Special case, field delimited by double starts is a place name
    // that should be removed from the message string
    match = PLACE.matcher(body);
    if (match.find()) {
      data.strPlace = body.substring(match.start(1), match.end(1));
      body = body.substring(0, match.start()+1) + body.substring(match.end());
    }
    
    String[] flds = body.split("\\*+");
    if (flds.length < 4) return false;
    
    Result lastResult = null;
    String lastFld = null;
    boolean mutualAid = true;
    int ndx = 0;
    for (String fld : flds) {
      fld = fld.trim();
      
      switch (ndx++) {
      
      case 0:
        data.strTime = fld;
        break;
      
      case 1:
        // Call description
        data.strCall = fld;
        mutualAid = fld.startsWith("Mutual Aid");
        break;
        
      case 2:
        // Address line
        
        // If line ends with intersection, it is positively the
        // address field.  Any previously found field goes into the place
        // field, and we process the next intersecting address field.
        if (fld.endsWith(" INTERSECTN")) {
          if (lastFld != null) data.strPlace = lastFld;
          parseAddress(StartType.START_ADDR, fld.substring(0, fld.length()-11), data);
          data.strApt = append(data.strApt, "-", getLeft());
          break;
        }
        
        // If mutual aid call, this is the only address
        // don't bother looking for a place field
        if (mutualAid) {
          parseAddress(fld, data);
          break;
        }
        
        // Otherwise parse the address.  We always parse the first two
        // fields to see which one has the best address
        Result result = parseAddress(StartType.START_ADDR, fld);
        if (lastResult == null) {
          lastFld = fld;
          lastResult = result;
          ndx--;
          break;
        }
        
        // If this field looks better than the previous one
        // treat the prev field as a place and and parse this an address;
        if (lastResult.getStatus() < result.getStatus()) {
          data.strPlace = lastFld;
          result.getData(data);
          data.strApt = append(data.strApt, "-", result.getLeft());
          break;
        }
        
        // If the previous field looks like the better than this one
        // parse the previous address and drop through to treat this
        // one as the first cross street
        lastResult.getData(data);
        data.strApt = append(data.strApt, "-", lastResult.getLeft());
        ndx++;
        
      case 3:
        // Cross streets * City

        // If identified city, process city field and progress to next UNIT field
        fld = fld.toUpperCase();
        if (CITY_LIST.contains(fld)) {
          data.strCity = fld;
          String newCity = CITY_CHANGES.getProperty(fld);
          if (newCity != null) {
            if (!newCity.equals("CHAR HALL") && data.strPlace.length() == 0) {
              data.strPlace = data.strCity;
            }
            data.strCity = newCity;
          }
          break;
        }
        
        // If identified unit field, drop through to next field
        if (isUnitField(fld)) {
          ndx++;
        }
        
        // Otherwise accumulate cross street and repeat this field
        else {
          data.strCross = append(data.strCross, " / ", fld);
          ndx--;
          break;
        }
        
      case 4:
        // Units
        data.strUnit = fld;
        break;
        
      case 5:
        // Description
        if (fld.startsWith("1. ")) {
          fld = INFO_BRK_PTN.matcher(fld).replaceAll("\n");
        }
        data.strSupp = append(data.strSupp, "\n", fld);
        ndx--;
        break;
      }
    }
    
    return true;
  }
  
  /*
   * Determine if field is city or unit field
   */
  private static final Pattern UNIT_PTN = Pattern.compile("[A-Z]+[0-9]+|ALS");
  private boolean isUnitField(String field) {
    for (String unit : field.split(" +")) {
      if (!UNIT_PTN.matcher(unit).matches()) return false;
    }
    return true;
  }
  
  @Override
  protected String adjustGpsLookupAddress(String address, String apt) {
    if (address.equals("46860 HILTON DR") && apt.length() > 2) {
      address = address + " BLDG " + apt.substring(0, apt.length()-2); 
    }
    return address;
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "45325 ABELL HOUSE LN",                 "+38.298550,-76.508292",
      "44100A LOUISDALE RD",                  "+38.294415,-76.558088",
      "44100B LOUISDALE RD",                  "+38.293484,-76.558780",
      "44605 BARNES AND YEH LANE",            "+38.289400,-76.532300",
      "44655 BARNES AND YEH LANE",            "+38.286800,-76.530900",
      "44665 BARNES AND YEH LANE",            "+38.286400,-76.531600",
      "44683 BARNES AND YEH LANE",            "+38.285600,-76.530600",
      "46155 CARVER SCHOOL BL",               "+38.250002,-76.480090",
      "43950 CASEYS LN",                      "+38.283806,-76.556252",
      "25390 COLTON POINT RD",                "+38.360118,-76.706042",
      "48017 CROSS MANOR RD",                 "+38.159970,-76.414850",
      "20845 DAISY LN",                       "+38.237791,-76.460179",
      "23520 FDR BOULEVARD",                  "+38.303844,-76.524341",
      "23550 FDR BOULEVARD",                  "+38.304432,-76.524862",
      "23580 FDR BOULEVARD",                  "+38.305154,-76.525273",
      "45910 FOXCHASE DR",                    "+38.255517,-76.489115",
      "45915 FOXCHASE DR",                    "+38.255538,-76.488573",
      "45925 FOXCHASE DR",                    "+38.255976,-76.488343",
      "45935 FOXCHASE DR",                    "+38.256480,-76.488265",
      "45955 FOXCHASE DR",                    "+38.256263,-76.489099",
      "45965 FOXCHASE DR",                    "+38.256115,-76.489767",
      "45975 FOXCHASE DR",                    "+38.255842,-76.490476",
      "45985 FOXCHASE DR",                    "+38.255207,-76.490182",
      "21412 GREAT MILLS RD BWING",           "+38.254868,-76.477345",
      "46860 HILTON DR BLDG 1",               "+38.247680,-76.459357",
      "46860 HILTON DR BLDG 2",               "+38.248052,-76.459638",
      "46860 HILTON DR BLDG 3",               "+38.248016,-76.460045",
      "46860 HILTON DR BLDG 4",               "+38.247862,-76.460461",
      "46860 HILTON DR BLDG 5",               "+38.248137,-76.460772",
      "46860 HILTON DR BLDG 6",               "+38.248310,-76.460351",
      "46860 HILTON DR BLDG 7",               "+38.248618,-76.460261",
      "46860 HILTON DR BLDG 8",               "+38.248898,-76.460566",
      "46860 HILTON DR BLDG 9",               "+38.249052,-76.460147",
      "46860 HILTON DR BLDG 10",              "+38.248774,-76.459841",
      "46860 HILTON DR BLDG 11",              "+38.248637,-76.459482",
      "46860 HILTON DR BLDG 12",              "+38.248869,-76.459048",
      "46860 HILTON DR BLDG 13",              "+38.246921,-76.455179",
      "46860 HILTON DR BLDG 14",              "+38.246892,-76.455582",
      "46860 HILTON DR BLDG 15",              "+38.246974,-76.456037",
      "46860 HILTON DR BLDG 16",              "+38.247192,-76.456301",
      "46860 HILTON DR BLDG 17",              "+38.247367,-76.456849",
      "46860 HILTON DR BLDG 18",              "+38.247198,-76.457187",
      "46860 HILTON DR BLDG 19",              "+38.247560,-76.457808",
      "46860 HILTON DR BLDG 20",              "+38.247340,-76.458201",
      "46860 HILTON DR BLDG 21",              "+38.247626,-76.458444",
      "46860 HILTON DR BLDG 22",              "+38.249001,-76.456838",
      "46860 HILTON DR BLDG 23",              "+38.248483,-76.456992",
      "46860 HILTON DR BLDG 24",              "+38.248172,-76.456569",
      "46860 HILTON DR BLDG 25",              "+38.247810,-76.456274",
      "46860 HILTON DR BLDG 26",              "+38.248028,-76.455913",
      "46860 HILTON DR BLDG 27",              "+38.248600,-76.455898",
      "46860 HILTON DR BLDG 28",              "+38.248697,-76.456372",
      "46860 HILTON DR BLDG 29",              "+38.248491,-76.455268",
      "46860 HILTON DR BLDG 30",              "+38.248461,-76.454921",
      "46860 HILTON DR BLDG 31",              "+38.248002,-76.455438",
      "46860 HILTON DR BLDG 32",              "+38.247738,-76.455525",
      "46860 HILTON DR BLDG 33",              "+38.247667,-76.455166",
      "46860 HILTON DR BLDG 34",              "+38.247891,-76.454839",
      "46860 HILTON DR BLDG 35",              "+38.248175,-76.454757",
      "22492 JOHNSON POND LANE",              "+38.285500,-76.549000",
      "22494 JOHNSON POND LANE",              "+38.285500,-76.549000",
      "22505 JOHNSON POND LANE",              "+38.286000,-76.550400",
      "22525 JOHNSON POND LANE",              "+38.286300,-76.550800",
      "22550 JOHNSON POND LANE",              "+38.286500,-76.550700",
      "45889 KETCH COURT",                    "+38.242582,-76.487140",
      "44096 LOUISDALE RD",                   "+38.296261,-76.562480",
      "44100 LOUISDALE RD",                   "+38.295197,-76.558400",
      "45770 MILITARY LANE",                  "+38.282760,-76.492340",
      "45772 MILITARY LANE",                  "+38.282890,-76.492300",
      "45774 MILITARY LANE",                  "+38.283020,-76.492440",
      "45776 MILITARY LANE",                  "+38.283240,-76.492330",
      "45778 MILITARY LANE",                  "+38.283340,-76.492180",
      "45780 MILITARY LANE",                  "+38.283340,-76.491810",
      "45782 MILITARY LANE",                  "+38.283200,-76.491060",
      "45790 MILITARY LANE",                  "+38.282720,-76.493240",
      "45792 MILITARY LANE",                  "+38.282850,-76.493030",
      "44994 NORRIS RD",                      "+38.273770,-76.507290",
      "44800 OAK CREST RD",                   "+38.311297,-76.527188",
      "22024 OXFORD CT",                      "+38.273351,-76.468718",
      "22025 OXFORD CT",                      "+38.273123,-76.468245",
      "22026 OXFORD CT",                      "+38.273393,-76.468547",
      "22027 OXFORD CT",                      "+38.273178,-76.468065",
      "22028 OXFORD CT",                      "+38.273434,-76.468369",
      "22030 OXFORD CT",                      "+38.273478,-76.468200",
      "48676 PACKER CT",                      "+38.155650,-76.391350",
      "43738 STEPHENSON DR",                  "+38.198215,-76.563438",
      "42831 THERESAS WY",                    "+38.368228,-76.595153",
      "22589 THREE NOTCH RD",                 "+38.287556,-76.480120",
      "45462 WEST MEATH WAY",                 "+38.288942,-76.503772",
      "45472 WEST MEATH WAY",                 "+38.288764,-76.503479",
      "45482 WEST MEATH WAY",                 "+38.288690,-76.503104",
      "45485 WEST MEATH WAY",                 "+38.288198,-76.503089",
      "45495 WEST MEATH WAY",                 "+38.288101,-76.502741",
      "45502 WEST MEATH WAY",                 "+38.288475,-76.502375",
      "45511 WEST MEATH WAY",                 "+38.287958,-76.502099",
      "45512 WEST MEATH WAY",                 "+38.288355,-76.502034",
      "45521 WEST MEATH WAY",                 "+38.287701,-76.502262",
      "45522 WEST MEATH WAY",                 "+38.288337,-76.501659"
  });
  
  private static Set<String> CITY_LIST = new HashSet<String>(Arrays.asList(new String[]{
      "CALIFORNIA",
      "CEDAR COVE",
      "CEDARCOVE",
      "CHAR HALL",
      "CHARLOTTE HALL",
      "CHESTNUT HILLS",
      "GOLDEN BEACH",
      "LEXINGTON PARK",
      "ABELL",
      "AVENUE",
      "BAREFOOT ACRES",
      "BEACHVILLE-ST INIGOES",
      "BUSHWOOD",
      "CALLAWAY",
      "CHAPTICO",
      "CLEMENTS",
      "COLTONS POINT",
      "COUNTRY LAKES",
      "COMPTON",
      "DAMERON",
      "DRAYDEN",
      "ESPERANZA FARMS",
      "FIRST COLONY",
      "GLEN FOREST NAWC",
      "GREAT MILLS",
      "HELEN",
      "HERMANVILLE",
      "HOLLYWOOD",
      "LAUREL GROVE",
      "LEONARDTOWN",
      "LEXINGTON PARK",
      "LORD CALVERT TRLPK",
      "LOVEVILLE",
      "MADDOX",
      "MECHANICSVILLE",
      "MEDLEYS NECK",
      "MORGANZA",
      "NEW MARKET",
      "OAKVILLE",
      "ORAVILLE",
      "PARK HALL",
      "PINEY POINT",
      "REDGATE",
      "RIDGE",
      "SAN SOUCI",
      "SCOTLAND",
      "SOUTH HAMPTON",
      "SPRING RIDGE",
      "ST INIGOES",
      "ST JAMES",
      "ST MARYS CITY",
      "TALL TIMBERS",
      "TOWN CREEK",
      "VALLEY LEE",
      "WILDEWOOD",
      
      "CALVERT COUNTY"
  }));
  
  private static final Properties CITY_CHANGES = buildCodeTable(new String[]{
      "CHAR HALL", "CHARLOTTE HALL",
      
      "BAREFOOT ACRES", "CALIFORNIA",
      "ESPERANZA FARMS","CALIFORNIA",
      "FIRST COLONY",   "CALIFORNIA",
      "SAN SOUCI",      "CALIFORNIA",
      "TOWN CREEK",     "CALIFORNIA",

      "CEDAR COVE",   "LEXINGTON PARK",
      "CEDARCOVE",    "LEXINGTON PARK",
      "GLEN FOREST NAWC", "LEXINGTON PARK",
      "LORD CALVERT TRLPK", "LEXINGTON PARK",
      "HERMANVILLE",  "LEXINGTON PARK",
      "SOUTH HAMPTON","LEXINGTON PARK",
      "SPRING RIDGE", "LEXINGTON PARK",
      "ST JAMES",     "LEXINGTON PARK",
      
      "MEDLEYS NECK", "LEONARDTOWN"
  });
}
