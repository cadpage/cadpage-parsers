package net.anei.cadpage.parsers.FL;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.StandardCodeTable;
import net.anei.cadpage.parsers.dispatch.DispatchA52Parser;

public class FLOrangeCountyBParser extends DispatchA52Parser {
  
  public FLOrangeCountyBParser() {
    super(CODE_TABLE, "ORANGE COUNTY", "FL");
  }
  
  @Override
  public String getFilter() {
    return "tap@yourdomain.com,@orlandohealth.com";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    if (data.strCode.startsWith("_")) {
      data.strCode = data.strCode.substring(1).trim();
      String call = CODE_TABLE.getCodeDescription(data.strCode);
      if (call != null) data.strCall = call;
    }
    return true;
  }
  
  private static final CodeTable CODE_TABLE = new StandardCodeTable();
}
