package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchA80Parser;

public class MOStFrancoisCountyBParser extends DispatchA80Parser {
  
  public MOStFrancoisCountyBParser() {
    this("ST FRANCOIS COUNTY", "MO");
  }
  
  public MOStFrancoisCountyBParser(String defCity, String defState) {
    super(defCity, defState);
  }
  
  @Override
  public String getAliasCode() {
    return "MOStFrancoisCountyB";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }
}
