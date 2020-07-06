package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.dispatch.DispatchBCParser;


public class KSNeoshoCountyAParser extends DispatchBCParser {

  public KSNeoshoCountyAParser() {
    super("NEOSHO COUNTY", "KS");
  }

  @Override
  public String getFilter() {
    return "NEOSHO911@OMNIGO.COM,NEOSHO911@ACEKS.COM";
  }

 }
