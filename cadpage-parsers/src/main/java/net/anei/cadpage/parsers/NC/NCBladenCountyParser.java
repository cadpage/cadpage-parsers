package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class NCBladenCountyParser extends DispatchSouthernParser {

  public NCBladenCountyParser() {
    super(CITY_LIST, "BLADEN COUNTY", "NC",
          DSFLG_ADDR | DSFLG_ADDR_TRAIL_PLACE | DSFLG_OPT_CODE | DSFLG_TIME);
  }

  @Override
  public String getFilter() {
    return "@bladenco.org";
  }
  
  private static final Pattern OCA_MASTER = Pattern.compile("BEMS OCA: (\\d{4}-\\d{5}) assigned to CFS: (\\d{4}-\\d{6}) (.*)");

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = OCA_MASTER.matcher(body);
    if (match.matches()) {
      setFieldList("ID ADDR APT CITY CALL");
      data.strCallId = match.group(1) + '/' + match.group(2);
      String addr = match.group(3);
      int pt = addr.indexOf(',');
      if (pt >= 0) {
        parseAddress(addr.substring(0,pt).trim(), data);
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, addr.substring(pt+1).trim(), data);
      } else {
        parseAddress(StartType.START_ADDR, addr, data);
      }
      data.strCall = getLeft();
      return true;
    } 
    else {
      return super.parseMsg(body, data);
    }
  }

  private static final String[] CITY_LIST = new String[]{

    // Towns
    "BLADENBORO",
    "CLARKTON",
    "DUBLIN",
    "EAST ARCADIA",
    "ELIZABETHTOWN",
    "TAR HEEL",
    "WHITE LAKE",

    // Census-designated places
    "BUTTERS",
    "KELLY",
    "WHITE OAK",

    // Unincorporated communities
    "ABBOTTSBURG",
    "AMMON",
    "AMMON FORD",
    "COLLY TOWNSHIP",
    "COUNCIL",
    "ROSINDALE",

    // Townships
    "ABBOTTSBURG",
    "BETHEL",
    "BLADENBORO",
    "BROWN MARSH",
    "CARVERS CREEK",
    "CENTRAL",
    "CLARKTON",
    "COLLY",
    "CYPRESS CREEK",
    "ELIZABETHTOWN",
    "EAST ARCADIA",
    "FRENCHES CREEK",
    "HOLLOW",
    "LAKE CREEK",
    "TARHEEL",
    "TURNBULL",
    "WHITE OAK",
    "DUBLIN",
    "WHITES CREEK",
    
    // Columbus County
    "RIEGELWOOD",
    
    // Cumberland County
    "FAYETTEVILLE",
    
    // Robeson County
    "ST PAULS",
    
    // Sampson County
    "HARRELLS"
    
  };
}
