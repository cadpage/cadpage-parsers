package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchBCParser;

public class MONodawayCountyParser extends DispatchBCParser {
  
  public MONodawayCountyParser() {
    super("NODAWAY COUNTY", "MO");
  }
  
  @Override
  public String getFilter() {
    return "JS239@OMNIGO.COM,JS239@MARYVILLEDPS.COM,NCAD@BC-EMS.COM,NODAWAYCOJAIL@GMAIL.COM,RUGBYFINN@GMAIL.COM,RUBYFINN@GMAIL.COM";
  }
}
