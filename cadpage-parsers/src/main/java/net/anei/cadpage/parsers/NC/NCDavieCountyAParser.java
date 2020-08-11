package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA3Parser;

/**
 * Davie County, NC
 */
public class NCDavieCountyAParser extends DispatchA3Parser {
  
  public NCDavieCountyAParser() {
    super("DAVIE COUNTY", "NC",
          "ID Address:ADDR! APT CH City:CITY! ( SELECT/2 X/Z+ Location:PLACE! Complaint:CODE! Description:CALL! Caller:NAME! Ph#:PHONE Units:UNIT! INFO/N+ Narr:INFO/N! INFO/N+ " + 
                                             "| INFO1 Type:CODE! CALL! INFO1 PH#:PHONE Units:UNIT | X/Z+? ( SKIP Location:INFO1! Complaint:CODE% Description:CALL% Caller:NAME PH#:PHONE Units:UNIT | Type:X! X INFO1+ PH#:CODE% Units:CALL% IRA:NAME PHONE UNIT% INFO+ NARR:INFO ) ) INFO+",
          FA3_NBH_PLACE_OFF | FA3_LANDMARK_PLACE_OFF | FA3_GEO_COMMENT_PLACE_OFF);
  }
  
  @Override
  public String getFilter() {
    return "911@co.davie.nc.us,911@daviecountync.gov";
  }
  
  @Override
  public String getSponsor() {
    return "Davie County";
  }

  @Override
  protected String getSponsorDateString() {
    return "03012016";
  }
  
  private static final Pattern MARKER = Pattern.compile("911:Call ?# *");
  private static final Pattern DELIM2 = Pattern.compile(" {2,}| (?=Address:|Complaint:|Description:|Caller:|Ph#:|Units:|Narr:)");

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MARKER.matcher(body);
    if (!match.lookingAt()) return false;
    body = body.substring(match.end());
    body = body.replace(" LocCmmt:", " Location:").replace(" Comp:", " Complaint:").replace(" Ph#:", " PH#:");
    body = body.replace("Desc:", "Description:");
    String[] flds = (body+' ').split("\\* ");
    if (flds.length < 7) flds = body.split("\n");
    if (flds.length >= 7) {
      setSelectValue("1");
      return parseFields(flds, data);
    }
    setSelectValue("2");
    return parseFields(DELIM2.split(body), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CITY")) return new MyCityField();
    return super.getField(name);
  }
  
  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("SELECT ONE")) return;
      super.parse(field, data);
    }
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    return WA_PTN.matcher(addr).replaceAll("WAY");
  }
  private static final Pattern WA_PTN = Pattern.compile("\\bWA\\b", Pattern.CASE_INSENSITIVE);
}
