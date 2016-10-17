package net.anei.cadpage.parsers.NJ;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH01Parser;


public class NJMercerCountyBParser extends DispatchH01Parser {
  public NJMercerCountyBParser() {
    super("MERCER COUNTY", "NJ",
          "( SELECT/SHORT CALL2 PRI2? ADDR2 SRC DATETIME2! INFO/N+" +
          "| SKIP? ( MARK1! Location:ADDR! Response_Type:CALL! AlarmLevel:PRI! Zone:MAP! HUNITS! UNIT/C+? CPINOTES! " +
                  "| MARK2 Location:ADDR! Response_Type:CALL! AlarmLevel:PRI! Name:UNIT! ) NOTES+ )");
  }
  
  @Override
  public String getFilter() {
    return "@franklinmo.net,DoNotReplyCAD@mercercounty.org";
  }
  
  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!subject.equals("Notification")) return false;
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    data.strCross = data.strCross.replaceAll(" *, *", " / ");
    return true;
  }

  @Override
  protected boolean parseFields(String[] fields, Data data) {
    
    // Mixed in with the normal HTML alerts, we have some hacked up HTML alerts
    // that come here as one field with newline separated data, possibly followed
    // by a second disclaimer field
    
    String select = "REG";
    if (fields.length <= 2) {
      select = "SHORT";
      fields = fields[0].split("\n");
    }
    setSelectValue(select);
    return super.parseFields(fields, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("MARK1")) return new SkipField("Mercer Fire IAR Rip And Run Report", true);
    if (name.equals("MARK2")) return new SkipField("Mercer Fire Fax Rip and Run", true);
    if (name.equals("HUNITS")) return new SkipField("Handling Units.*", true);
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("CPINOTES")) return new SkipField("CPI Notes.*", true);
    
    if (name.equals("CALL2")) return new MyCall2Field();
    if (name.equals("PRI2")) return new PriorityField("\\d", true);
    if (name.equals("ADDR2")) return new MyAddress2Field();
    if (name.equals("DATETIME2")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
  
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(' ', '_');
      super.parse(field, data);
    }
  }
  
  private class MyCall2Field extends CallField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "\ufeff");
      field = stripFieldStart(field, "x");
      super.parse(field, data);
    }
  }
  
  private static final Pattern ADDRESS_PTN1 = Pattern.compile("&(.*?)\\((.*)\\) *, *(.*)");
  private static final Pattern ADDRESS_PTN2 = Pattern.compile("([^,]+),([ A-Z]*)(?:\\((.*)\\))?");
  private class MyAddress2Field extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDRESS_PTN1.matcher(field);
      if (match.matches()) {
        data.strPlace = match.group(1).trim();
        parseAddress(match.group(2).trim(), data);
        data.strCity = match.group(3).trim();
        return;
      }
      
      match = ADDRESS_PTN2.matcher(field);
      if (match.matches()) {
        parseAddress(match.group(1).trim(), data);
        data.strCity = match.group(2).trim();
        String cross = getOptGroup(match.group(3));
        cross = cross.replace(',', '/');
        cross = stripFieldStart(cross, "/");
        cross = stripFieldEnd(cross, "/");
        data.strCross = cross;
        return;
      }
      
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE ADDR APT CITY X";
    }
  }
}