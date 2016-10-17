package net.anei.cadpage.parsers.NJ;


import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchChiefPagingParser;

/**
 * Salem County, NJ (Deepwater)
 */
public class NJSalemCountyBParser extends DispatchChiefPagingParser {
  
  private static final Pattern PENNVILLE_PTN = Pattern.compile("\\bPennville\\b", Pattern.CASE_INSENSITIVE);

  public NJSalemCountyBParser() {
    super(CITY_LIST, "SALEM COUNTY", "NJ");
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // Drop calls from New Castle County, DE (B)
    if (body.contains(" EMD:")) return false;
    
    // Fix misspelled township name
    body = PENNVILLE_PTN.matcher(body).replaceAll("Pennsville");
 
    return super.parseMsg(subject, body, data);
  }
  
  private static String[] CITY_LIST = new String[]{

    "ALLOWAY",
    "CARNEYS POINT",
    "ELMER",
    "FRIENDSHIP",
    "HANCOCKS BRIDGE",
    "HARMONY",
    "QUINTON",
    "OLIVET",
    "PEDRICKTOWN",
    "PENNSVILLE",
    "POINTERS",
    "SALEM",
    "WOODSTOWN",
    "PENNS GROVE",

    "ALLOWAY TOWNSHIP",
    "CARNEYS POINT TOWNSHIP",
    "ELSINBORO TOWNSHIP",
    "LOWER ALLOWAYS CREEK TOWNSHIP",
    "MANNINGTON TOWNSHIP",
    "OLDMANS TOWNSHIP",
    "PENNSVILLE TOWNSHIP",
    "PILESGROVE TOWNSHIP",
    "PITTSGROVE TOWNSHIP",
    "QUINTON TOWNSHIP",
    "UPPER PITTSGROVE TOWNSHIP",
    
    // New Castle County, DE
    "NEW CASTLE",
    
    // Sussex County, DE
    "DELMAR",
    
    // Wicomico County, MD
    "PARSONSBURG"

  };
}


