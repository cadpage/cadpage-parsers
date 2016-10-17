package net.anei.cadpage.parsers.NC;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA3Parser;

/**
 * Davie County, NC
 */
public class NCDavieCountyParser extends DispatchA3Parser {
  
  public NCDavieCountyParser() {
    super(Pattern.compile("^911:Call ?#"), "DAVIE COUNTY", "NC",
           "ID Address:ADDR! APT CH! City:CITY! ( INFO1 Type:CODE! CALL! INFO1 PH#:PHONE Units:UNIT | X/Z+? ( SKIP Location:INFO1! Complaint:CODE% Description:CALL% Caller:NAME PH#:PHONE Units:UNIT | Type:X! X INFO1+ PH#:CODE% Units:CALL% IRA:NAME PHONE UNIT% INFO+ NARR:INFO ) ) INFO+",
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

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace(" LocCmmt:", " Location:").replace(" Comp:", " Complaint:").replace(" Ph#:", " PH#:");
    body = body.replace("Desc:", "Description:");
    return super.parseMsg(body, data);
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    return WA_PTN.matcher(addr).replaceAll("WAY");
  }
  private static final Pattern WA_PTN = Pattern.compile("\\bWA\\b", Pattern.CASE_INSENSITIVE);
}
