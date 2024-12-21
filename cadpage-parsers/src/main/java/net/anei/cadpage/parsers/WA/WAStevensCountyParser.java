package net.anei.cadpage.parsers.WA;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;



public class WAStevensCountyParser extends DispatchA19Parser {

  public WAStevensCountyParser() {
    super("STEVENS COUNTY", "WA");
  }

  @Override
  public String getFilter() {
    return "spillmancad@co.stevens.wa.us,Dispatch,@lifeflight.org";
  }
}
