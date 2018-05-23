package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class OHMuskingumCountyCParser extends DispatchH05Parser {
  
  public OHMuskingumCountyCParser() {
    super("MUSKINGUM COUNTY", "OH", 
          "Address:ADDRCITY/S6! CALL_DATETIME_ID! Cross_Streets:X! Incident_number:ID2! Narrative:EMPTY! INFO_BLK+ Times:EMPTY TIMES+ Final_Report:SKIP");
  }
  
  @Override
  public String getFilter() {
    return "dispatch2@ohiomuskingumsheriff.org";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("CALL_DATETIME_ID")) return new MyCallDateTimeIdField();
    if (name.equals("ID2")) return new MyId2Field();
    return super.getField(name);
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '/');
      super.parse(field, data);
      if (data.strApt.length() > 0) {
        data.strCity = stripFieldEnd(data.strCity, data.strApt);
      }
    }
  }
  
  private static final Pattern CALL_DATETIME_ID_PTN = Pattern.compile("Call Type:(.*)Date/Time:(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d:\\d\\d:\\d\\d) *CFS#:(.*)");
  private class MyCallDateTimeIdField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_DATETIME_ID_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCall =  match.group(1).trim();
      data.strDate = match.group(2);
      data.strTime = match.group(3);
      data.strCallId = match.group(4).trim();
    }

    @Override
    public String getFieldNames() {
      return "CALL DATE TIME ID";
    }
  }
  
  private class MyId2Field extends IdField {
    @Override
    public void parse(String field, Data data) {
      StringBuilder sb = new StringBuilder();
      for (String part : field.split(",")) {
        part = part.trim();
        part = stripFieldStart(part, "[");
        part = stripFieldEnd(part, "]");
        if (part.startsWith("Incident not yet created")) continue;
        if (sb.length() > 0) sb.append(", ");
        sb.append(part);
      }
      if (sb.length() > 0) {
        if (data.strCallId.length() > 0) {
          sb.append(" / ");
          sb.append(data.strCallId);
        }
        data.strCallId = sb.toString();
      }
    }
  }
}
