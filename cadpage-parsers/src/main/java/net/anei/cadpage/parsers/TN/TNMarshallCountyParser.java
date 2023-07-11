package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class TNMarshallCountyParser extends DispatchA74Parser {

  public TNMarshallCountyParser() {
    super("MARSHALL COUNTY", "TN");
  }

  @Override
  public String getFilter() {
    return "Dispatch@marshallTNE911.info";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

}
