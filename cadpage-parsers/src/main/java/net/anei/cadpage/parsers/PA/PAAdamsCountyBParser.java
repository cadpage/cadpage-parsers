package net.anei.cadpage.parsers.PA;

public class PAAdamsCountyBParser extends PAFranklinCountyParser {
  
  // The Franklin County parser was originally identified as an alternate Adams County
  // parser.  So we keep this alias so people using that original parser will still work
  
  public PAAdamsCountyBParser() {
    super("ADAMS COUNTY", "PA");
  }

}
