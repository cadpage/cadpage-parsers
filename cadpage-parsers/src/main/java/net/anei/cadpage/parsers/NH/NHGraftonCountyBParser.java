package net.anei.cadpage.parsers.NH;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA32Parser;

public class NHGraftonCountyBParser extends DispatchA32Parser {

  public NHGraftonCountyBParser() {
    this("GRAFTON COUNTY", "NH");
  }

  protected NHGraftonCountyBParser(String defCity, String defState) {
    super(NHGraftonCountyParser.CITY_LIST, defCity, defState);
  }

  @Override
  public String getAliasCode() {
    return "NHGraftonCountyB";
  }

  @Override
  public String getFilter() {
    return "dispatch@co.grafton.nh.us,lincolnpd546@gmail.com,lincolnpd546@lincolnnh.org,lebanonpaging@gmail.com,cpddispatch@claremontnh.com,hanoverdispatch@gmail.com,Dispatch@lebanonnh.gov";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.equals("CCSO") || subject.equals("_")) subject += " Page";
    if (!super.parseMsg(subject, body, data)) return false;
    data.strCity = stripFieldEnd(data.strCity, "Sheriff");
    return true;
  }
}
