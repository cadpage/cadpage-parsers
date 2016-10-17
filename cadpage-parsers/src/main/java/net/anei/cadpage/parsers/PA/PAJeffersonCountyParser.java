package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;



public class PAJeffersonCountyParser extends SmartAddressParser {
  
  private static final Pattern CLOSE_PAREN_BLK_PTN = Pattern.compile("\\)([^ ])");
  private static final Pattern BOX_CH_PTN = Pattern.compile("(?: +BOX|-)? +(\\d{1,2}-[A-Z](?:-[A-Z])?)(?: +([A-Za-z]+(?: +[A-Za-z]+)?))?$");
  private static final Pattern UNIT_PTN = Pattern.compile("(?: +(?:[A-Z]+\\d+(?:-\\d+)?|HH))+  +");
  private static final Pattern GPS_PIPE_PTN1 = Pattern.compile("^([-+]?\\d+\\.\\d+)\\|([-+]?\\d+\\.\\d+)\\b");
  private static final Pattern GPS_PIPE_PTN2 = Pattern.compile("\\b([-+]?\\d+\\.\\d+)\\|([-+]?\\d+\\.\\d+)\\b");
  private static final Pattern ADDR_EXT_PTN = Pattern.compile("[NSEW]|EXT");
  
  public PAJeffersonCountyParser() {
    super(CITY_LIST, "JEFFERSON COUNTY", "PA");
    setupMultiWordStreets(MULTI_WORD_STREETS);
    removeWords("BUS");
    setFieldList("CALL UNIT ADDR APT CITY PLACE X INFO BOX CH");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@jeffersoncountypa.com";
  }
  
  @Override
  public String adjustMapAddress(String address) {
    return PENN_STREET_EXT.matcher(address).replaceAll("PENN ST EXD");
  }
  private static final Pattern PENN_STREET_EXT = Pattern.compile("\\bPENN ST(?:REET)? EXT?\\b", Pattern.CASE_INSENSITIVE);

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("Incident")) return false;
    
    int pt = body.indexOf("\n--");
    if (pt >= 0) body = body.substring(0,pt).trim();

    body = CLOSE_PAREN_BLK_PTN.matcher(body).replaceFirst(") $1");
    Matcher match = BOX_CH_PTN.matcher(body);
    if (match.find()) {
      data.strBox = match.group(1);
      data.strChannel = getOptGroup(match.group(2));
      body = body.substring(0,match.start()).trim();
    }
    
    pt = body.indexOf("Narrative:");
    if (pt >= 0) {
      data.strSupp = body.substring(pt+10).trim();
      body = body.substring(0,pt).trim();
    }
    
    // See if we can find a unit field separating the call description from the address
    StartType st = StartType.START_CALL;
    int flags = FLAG_START_FLD_REQ;
    match = UNIT_PTN.matcher(body);
    if (match.find()) {
      data.strCall = body.substring(0,match.start());
      data.strUnit = match.group().trim();
      body = body.substring(match.end());
      st = StartType.START_ADDR;
      flags = 0;
    }
    
    // GPS coordinates use an unusual pipe separator that the smart address parser will not recognize
    String extra;
    Pattern ptn = (st == StartType.START_ADDR ? GPS_PIPE_PTN1 : GPS_PIPE_PTN2);
    match = ptn.matcher(body);
    if (match.find()) {
      if (st == StartType.START_CALL) data.strCall = body.substring(0,match.start()).trim();
      data.strAddress = match.group(1) + "," + match.group(2);
      extra = body.substring(match.end()).trim();
    }
    
    // If no GPS coordinates found, use the smart address parser to split things
    else {
  
      // SNYDER HILL in info section confuses the smart address parer
      body = body.replace("Snyder Hill", "Snyder-Hill");
      body = body.replace(",", " ,");
      parseAddress(st, flags | FLAG_PAD_FIELD_EXCL_CITY | FLAG_CROSS_FOLLOWS, body, data);
      String pad = getPadField();
      if (pad.length() > 0) {
        if (ADDR_EXT_PTN.matcher(pad).matches()) {
          data.strAddress = append(data.strAddress, " ", pad);
        } else if (!pad.contains(" ")) {
          data.strApt = append(data.strApt, "-", pad);
        } else {
          data.strCross = getPadField();
        }
      }
      extra = getLeft().replace(" ,", ",");
    }
    
    boolean incComma = false;
    if (data.strCross.length() > 0) {
      data.strPlace = extra;
    } else {
      for (String part : extra.split(",")) {
        part = part.trim();
        if (data.strCross.length() > 0) {
          data.strCross = append(data.strCross, ", ", part);
        } else {
          Result res = parseAddress(StartType.START_PLACE, FLAG_ONLY_CROSS | FLAG_ANCHOR_END, part);
          if (res.isValid()) {
            String savePlace = data.strPlace;
            data.strPlace = "";
            res.getData(data);
            incComma = savePlace.length() > 0 && data.strPlace.length() == 0;
            data.strPlace = append(savePlace, ", ", data.strPlace);
          } else {
            data.strPlace = append(data.strPlace, ", ", part);
          }
        }
      }
    }
    
    // The worst part is when they do not include a space separator between
    // the place name and the cross street.  We will go through a lot of
    // work to straighten that one out
    if (data.strPlace.length() > 0 && data.strCross.length() > 0) {
      fixPlaceCrossFields(data, incComma);
    }
    
    return true;
  }
  
  private void fixPlaceCrossFields(Data data, boolean incComma) {
    
    // First see if the cross street starts with something that looks
    // like it belongs in the place field
    for (String tmp : PLACE_END_NAMES) {
      if (data.strCross.startsWith(tmp)) {
        data.strPlace = data.strPlace + (incComma ? ", " : " ") + tmp;
        data.strCross = data.strCross.substring(tmp.length()).trim();
        return;
      }
    }
    
    // No luck there.  See if the last word of the place field starts with
    // one of the end place words
    int pt = data.strPlace.lastIndexOf(' ');
    if (pt >= 0) {
      String word = data.strPlace.substring(pt+1).trim();
      for (String placeWord : PLACE_END_NAMES) {
        if (word.startsWith(placeWord)) {
          
          // Looking good, but there are some other conditions that need
          // to be satisfied.  Like maing sure there is some more to this
          // work that we can transfer to the cross street
          String extra = word.substring(placeWord.length()).trim();
          if (extra.length() == 0) return;
          String tmpPlace = data.strPlace.substring(0,pt+placeWord.length()+1).trim();
          String tmpCross = extra + ' ' + data.strCross;
          
          // If the extra stuff is a single character direction, go for it
          if (extra.length() == 1 && "NSEW".indexOf(extra.charAt(0)) >= 0) {
            data.strPlace = tmpPlace;
            data.strCross = tmpCross;
            return;
          }
          
          // Likewise if the resulting cross street starts with a multi-word
          // street name
          for (String mStreet : MULTI_WORD_STREETS) {
            if (tmpCross.startsWith(mStreet)) {
              data.strPlace = tmpPlace;
              data.strCross = tmpCross;
              return;
            }
          }
          
          // Otherwise, leave things alone
          return;
        }
      }
    }
  }
  
  private static final String[] PLACE_END_NAMES = new String[]{
    "#2",
    "APARTMENTS",
    "CATERING",
    "COMPANY",
    "FEZELL",
    "INN",
    "KNOB",
    "LIVING",
    "PHOTOGRAPHY",
    "PUNXSUTAWNEY",
    "RESTAURANT",
    "SCHOOL",
    "SONS",
    "TOWERS",
    "WARDEN"
  };
  
  private static final String[] MULTI_WORD_STREETS = new String[] {
    "ALLENS MILLS",
    "CANOE RIDGE",
    "CLOE CHURCH",
    "CLOE ROSSITER",
    "DUG HILL",
    "EAGLES NEST",
    "ELDERBERRY HILL",
    "GAME SCHOOL",
    "GRANGE HALL",
    "GREATER INAGUA",
    "HEMLOCK LAKE",
    "HIDDEN HOLLOW",
    "HORKINS MILL",
    "JIM TOWN",
    "LITTLE INAGUA",
    "KNOX DALE",
    "MARSH HOLLOW",
    "PANIC WISHAW", 
    "PANSY RINGGOLD",
    "REYNOLDSVILLE FALLS CREEK",
    "RUSTY COAT", 
    "SANDY VALLEY",
    "SPRING CREEK",
    "SWARTZ ACRES",
    "VAN WOERT",
    "VO TECH",
    "WALTER LONG",
    "WATER PLANT",
  };
  
  private static final String[] CITY_LIST = new String[]{
    
    // Boroughs
    "BIG RUN",
    "BROCKWAY",
    "BROOKVILLE",
    "CORSICA",
    "FALLS CREEK",
    "PUNXSUTAWNEY",
    "REYNOLDSVILLE",
    "SUMMERVILLE",
    "SYKESVILLE",
    "TIMBLIN",
    "WORTHVILLE",

    // Townships
    "BARNETT",
    "BEAVER",
    "BELL",
    "CLOVER",
    "ELDRED",
    "GASKILL",
    "HEATH",
    "HENDERSON",
    "KNOX",
    "MCCALMONT",
    "OLIVER",
    "PERRY",
    "PINE CREEK",
    "POLK",
    "PORTER",
    "RINGGOLD",
    "ROSE",
    "SNYDER",
    "UNION",
    "WARSAW",
    "WASHINGTON",
    "WINSLOW",
    "YOUNG",
    
    // Unincoroporated community
    "BANKS",
    "SPRANKLE MILLS",
    "CANOE",
    
    // Clearfield County
    "SANDY",
    
    // Indiana County
    "BRUSH VALLEY",
    "NORTH MAHONING"

  };
}
