package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class PAMcKeanCountyDParser extends DispatchH05Parser {

  public PAMcKeanCountyDParser() {
    super("MCKEAN COUNTY", "PA",
          "Inc_Code:CALL! Address:ADDRCITY! Common_Name:PLACE! Units:UNIT! Cross_Streets:X! Alert_Code:SRC");
  }

}
