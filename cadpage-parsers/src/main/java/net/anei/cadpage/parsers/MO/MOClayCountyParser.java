package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.GroupBestParser;
import net.anei.cadpage.parsers.GroupBlockParser;


public class MOClayCountyParser extends GroupBestParser {

  // MOClayCountC uses DispatchA33Parser
  // MOClayCountD used DispatchBCParser which can call DispatchA33 as a backup, but 
  // that is not needed for MOClayCountyD.  Since the two are using different dispatch
  // centers with different email addresses, we will separate them with a block call.
  public MOClayCountyParser() {
    super(new MOClayCountyAParser(), new MOClayCountyCParser(), 
          new MOClayCountyEParser(),
          new GroupBlockParser(),
          new MOClayCountyDParser());
  }
}
