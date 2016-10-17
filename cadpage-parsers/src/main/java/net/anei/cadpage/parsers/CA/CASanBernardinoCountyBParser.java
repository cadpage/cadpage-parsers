package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.StandardCodeTable;

/**
 * San Bernardino County, CA (B)
 * Obsolete - replace with version (D)
 */
public class CASanBernardinoCountyBParser extends FieldProgramParser {
  
  public CASanBernardinoCountyBParser() {
    super("SAN BERNARDINO COUNTY", "CA",
          "CODE ADDR GPS ID! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@csb.gov";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Incident")) return false;
    return super.parseFields(body.split(";"),  data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{4}-\\d{8}", true);
    if (name.equals("CODE")) return new MyCodeField();
    return super.getField(name);
  }
  
  private class MyCodeField extends CodeField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field, data);
      String call = CALL_CODES.getCodeDescription(field);
      if (call != null) data.strCall = call;
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
  
  private static final CodeTable CALL_CODES = new StandardCodeTable();
}
