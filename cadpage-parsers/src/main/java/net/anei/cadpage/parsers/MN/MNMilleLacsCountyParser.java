package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA63Parser;

public class MNMilleLacsCountyParser extends DispatchA63Parser {
  
  public MNMilleLacsCountyParser() {
    super(CITY_LIST, "MILLE LACS COUNTY", "MN");
  }
  
  @Override
  public String getFilter() {
    return "MLCJ.Dispatch@millelacs.mn.gov";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    int pt = data.strAddress.indexOf(';');
    if (pt >= 0) {
      data.strAddress = data.strAddress.substring(0,pt).trim() + '(' + data.strAddress.substring(pt+1).trim() + ')';
    }
    return true;
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "BOCK",
      "FORESTON",
      "ISLE",
      "MILACA",
      "ONAMIA",
      "PEASE",
      "PRINCETON",
      "WAHKON",
      
      // Census-designated place
      "VINELAND",
      
      // Unincorporated communities
      "BAYVIEW",
      "COVE",
      "ESTES BROOK",
      "LONG SIDING",
      "OPSTEAD",
      "PAGE",
      "WOODWARD BROOK",
      
      // Ghost towns
      "BRICKTON",
      "BURNHELM SIDING",
      "ESTEVILLE",
      "FREER",
      "JOHNSDALE",
      "SOULES CROSSING",
      "STIRLING",
      
      // Townships
      "BOGUS BROOK TOWNSHIP",
      "BORGHOLM TOWNSHIP",
      "BRADBURY TOWNSHIP",
      "DAILEY TOWNSHIP",
      "EAST SIDE TOWNSHIP",
      "GREENBUSH TOWNSHIP",
      "HAYLAND TOWNSHIP",
      "ISLE HARBOR TOWNSHIP",
      "KATHIO TOWNSHIP",
      "LEWIS TOWNSHIP",
      "MILACA TOWNSHIP",
      "MILO TOWNSHIP",
      "MUDGETT TOWNSHIP",
      "ONAMIA TOWNSHIP",
      "PAGE TOWNSHIP",
      "PRINCETON TOWNSHIP",
      "ROOSEVELT TOWNSHIP",
      "SOUTH HARBOR TOWNSHIP",
  };
}
