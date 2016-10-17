package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchCiscoParser;

/**
 * McDuffie County, GA
 */
public class GAMcDuffieCountyParser extends DispatchCiscoParser {

  public GAMcDuffieCountyParser() {
    super("MCDUFFIE COUNTY", "GA");
  }

  @Override
  public String getFilter() {
    return "cisco@thomson-mcduffie.net";
  }

}
  