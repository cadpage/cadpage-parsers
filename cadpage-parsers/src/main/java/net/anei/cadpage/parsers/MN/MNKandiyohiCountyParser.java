package net.anei.cadpage.parsers.MN;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA43Parser;

public class MNKandiyohiCountyParser extends DispatchA43Parser {
  
  public MNKandiyohiCountyParser() {
    super(CITY_LIST, "KANDIYOHI COUNTY", "MN");
  }
  
  private static final String[] CITY_LIST = new String[]{

    // Cities
    "ATWATER",
    "BLOMKEST",
    "KANDIYOHI",
    "LAKE LILLIAN",
    "NEW LONDON",
    "PENNOCK",
    "PRINSBURG",
    "RAYMOND",
    "REGAL",
    "SPICER",
    "SUNBURG",
    "WILLMAR",

    // Townships
    "ARCTANDER",
    "BURBANK",
    "COLFAX",
    "DOVRE",
    "EAST LAKE LILLIAN",
    "EDWARDS",
    "FAHLUN",
    "GENNESSEE",
    "GREEN LAKE",
    "HARRISON",
    "HOLLAND",
    "IRVING",
    "KANDIYOHI",
    "LAKE ANDREW",
    "LAKE ELIZABETH",
    "LAKE LILLIAN",
    "MAMRE",
    "NEW LONDON",
    "NORWAY LAKE",
    "ROSELAND",
    "ROSEVILLE",
    "ST JOHNS",
    "WHITEFIELD",
    "WILLMAR",

    // Unincorporated communities
    "HAWICK",
    "PRIAM",
    "ROSELAND",
    
    // Neighborhoods
    "ALMOND",
    
    // Big Stone County
    "BEARDSLEY",
    "FOSTER"
  };
  
  private static final Set<String> NO_MAP_CITIES = new HashSet<String>(Arrays.asList(new String[]{
      "ALMOND",
      "FOSTER"
  }));
}
