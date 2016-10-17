package net.anei.cadpage.parsers.SC;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;

/**
 * Anderson County, SC (B) 
 */
public class SCAndersonCountyBParser extends DispatchA48Parser {
  
  public SCAndersonCountyBParser() {
    super(SCAndersonCountyParser.CITY_LIST, "ANDERSON COUNTY", "SC", FieldType.NONE, A48_ONE_WORD_CODE, 
          Pattern.compile("\\d{1,3}-[A-Z]+"));
    setupProtectedNames("SIX AND TWENTY");
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }
}
