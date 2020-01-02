package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;

public class ILRandolphCountyBParser extends DispatchA33Parser {

  public ILRandolphCountyBParser() {
    super("RANDOLPH COUNTY", "IL");
  } 

  @Override
  public String getFilter() {
    return "MONROECO@OMNIGO.COM";
  }
}
