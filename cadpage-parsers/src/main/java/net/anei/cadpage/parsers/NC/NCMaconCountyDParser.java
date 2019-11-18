package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;

public class NCMaconCountyDParser extends DispatchA48Parser {

  public NCMaconCountyDParser() {
    super(NCMaconCountyParser.CITY_LIST, "MACON COUNTY", "NC", FieldType.GPS_PHONE_NAME);
  }
  
  @Override
  public String getFilter() {
    return "2183500429";
  }
  
  private static final Pattern LEAD_SRC_PTN = Pattern.compile("([A-Z ]+) TEXT:");
  private static final Pattern TRAIL_UNIT_PTN = Pattern.compile(" {2,}([ A-Z0-9,]+)$");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    body = stripFieldEnd(body, "Stop");
    body = stripFieldEnd(body, "~");
    int tlen;
    do {
      tlen = body.length();
      body = stripFieldEnd(body, "\nnull");
      body = stripFieldEnd(body, "\nn");
    } while (body.length() < tlen);
    
    body = stripFieldStart(body, "MACON 911: ");
    body = stripFieldStart(body, "MACON CO 911: ");
    
    Matcher match = LEAD_SRC_PTN.matcher(body);
    if (match.lookingAt()) {
      subject = match.group(1);
    }

    String unit = "";
    match = TRAIL_UNIT_PTN.matcher(body);
    if (match.find()) {
      unit = match.group(1);
      body = body.substring(0,match.start());
    }
    
    if (!super.parseMsg(subject, body, data)) return false;

    data.strCall = stripFieldEnd(data.strCall, " REPORTED AT");
    data.strUnit = append(data.strUnit, ", ", unit);
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram() + " UNIT";
  }
  
}
