package net.anei.cadpage.parsers.dispatch;

import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Base class for handling page formats that have a main part followed by
 * a DispatchA3Parser style info section.
 */
public abstract class DispatchA3AParser extends DispatchA3Parser {
  
  public DispatchA3AParser(String defCity, String defState) {
    super(defCity, defState, null);
  }
  
  public DispatchA3AParser(String[] cityList, String defCity, String defState) {
    super(cityList, defCity, defState, null);
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    int brk = findInfoBreak(body);
    if (!parseMain(body.substring(0,brk), data)) return false;
    String info = body.substring(brk);
    if (info.length() > 0) getInfoField().parse(info, data);
    return true;
  }

  protected abstract boolean parseMain(String body, Data data);
  
  protected String getFieldList() {
    return getInfoField().getFieldNames();
  }
  
  private Field getInfoField() {
    if (infoField == null) infoField = getField("INFO");
    return infoField;
  }
  
  private Field infoField = null;
}
