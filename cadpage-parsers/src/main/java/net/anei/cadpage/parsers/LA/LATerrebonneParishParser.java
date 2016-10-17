package net.anei.cadpage.parsers.LA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.HtmlDecoder;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA13Parser;

public class LATerrebonneParishParser extends DispatchA13Parser {
  
  private Field addrField;
  private HtmlDecoder htmlDecoder = new HtmlDecoder();

  public LATerrebonneParishParser() {
    super("TERREBONNE PARISH", "LA");
    addrField = getField("ADDR");
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d/\\d\\d/\\d{4}) (\\d\\d:\\d\\d)");

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    
    if (!body.startsWith("<STYLE>")) return super.parseHtmlMsg(subject, body, data);
    
    FieldParser p = new FieldParser(htmlDecoder.parseHtml(body));
    
    if (!p.checkNextField("Completed Incident Report")) return false;
    if (!p.checkNextField("Response")) return false;

    setFieldList(addrField.getFieldNames() + " MAP CALL PRI ID DATE TIME INFO");
    
    String addr = p.getNextField("Location:");
    if (addr == null) return false;
    addrField.parse(addr, data);
    
    data.strMap = p.getNextField("Zone:");
    if (data.strMap ==  null) return false;
    
    data.strCall = p.getNextField("Response Type:");
    if (data.strCall == null) return false;
    
    if (p.getNextField("CreationTime:") == null) return false;
    
    String priority = p.getNextField("Priority:");
    String level = p.getNextField("AlarmLevel:");
    if (priority == null || level == null) return false;
    data.strPriority = append(priority, "/", level);
    
    data.strCallId = p.getNextField("SequenceNumber:");
    if (data.strCallId == null) return false;
    
    if (!p.searchField("Incident Notes")) return true;
    if (!p.checkNextField("TimeStamp")) return false;
    if (!p.checkNextField("Info")) return false;
    while (true) {
      String dateTime = p.getNextField();
      Matcher match = DATE_TIME_PTN.matcher(dateTime);
      if (!match.matches()) break;
      data.strDate = match.group(1);
      data.strTime = match.group(2);
      
      String info = p.getNextField();
      if (info == null) return false;
      data.strSupp = append(data.strSupp, "\n", info);
    }
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
  
  private class MyAddressField extends BaseAddressField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field, data);
      data.strPlace = append(data.strPlace, " - ", data.strCity);
      data.strCity = "";
    }
  }
  
  private static class FieldParser {
    private String flds[];
    private int fldNdx;
    
    public FieldParser(String[] flds) {
      this.flds = flds;
    }
    
    public String getNextField() {
      if (fldNdx >= flds.length) return null;
      return flds[fldNdx++];
    }
    
    public boolean checkNextField(String expField) {
      String field = getNextField();
      return (field != null && field.equals(expField));
    }
    
    public String getNextField(String label) {
      String field = getNextField();
      if (field == null) return null;
      if (!field.startsWith(label)) return null;
      return field.substring(label.length()).trim();
    }
    
    public boolean searchField(String expField) {
      while (true) {
        String field = getNextField();
        if (field == null) return false;
        if (field.equals(expField)) return true;
      }
    }
  }
}
