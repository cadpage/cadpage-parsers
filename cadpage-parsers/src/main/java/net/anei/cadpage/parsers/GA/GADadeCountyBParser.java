package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class GADadeCountyBParser extends DispatchH05Parser {
  
  public  GADadeCountyBParser() {
    super("DADE COUNTY", "GA", 
          "DATETIME ID ADDRCITY FIRE_CALL_TYPE EMS_CALL_TYPE! UNIT TABLE! TIMES+? TABLE< INFO_BLK+", "table");
  }
  
  @Override
  public String getFilter() {
    return "E911@dadega.com";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("Incident # *(.*)", true);
    if (name.equals("ADDRCITY")) return new AddressCityField("Location +(.*)", true);
    if (name.equals("FIRE_CALL_TYPE")) return new MyCallField("Fire Call Type");
    if (name.equals("EMS_CALL_TYPE")) return new MyCallField("EMS Call Type");
    if (name.equals("TABLE")) return new SkipField("<table>");
    return super.getField(name);
  }
  
  private class MyCallField extends CallField {
    private String label;
    
    public MyCallField(String label) {
      this.label = label;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!field.startsWith(label)) abort();
      field = field.substring(label.length()).trim();
      if (!field.equals(data.strCall)) data.strCall = append(data.strCall, "/", field);
    }
  }
}
