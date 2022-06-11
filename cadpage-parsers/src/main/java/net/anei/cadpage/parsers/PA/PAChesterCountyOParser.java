package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.HtmlDecoder;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PAChesterCountyOParser extends PAChesterCountyBaseParser {
  
  public PAChesterCountyOParser() {
    super("( SELECT/1 Call_Time:DATETIME1/d! EMPTY! Event:ID! Event_Type_Code:CALL! Event_Subtype_Code:CALL/SDS! ESZ:MAP! Beat:BOX! " + 
              "Address:EMPTY! PLACE! ADDR! Cross_Street:X! Location_Information:INFO! Development:INFO/N! Municipality:CITY! " + 
              "Caller_Information:INFO/N! Caller_Name:NAME! Caller_Phone_Number:PHONE! Alt_Phone_Number:SKIP! Caller_Address:SKIP! " + 
              "Caller_Source:SKIP! Units:UNIT! UNIT/S+ Event_Comments:INFO/N+ " + 
          "| SKIP+? Event_ID:EMPTY! ID! Event:EMPTY! ID2! Unit:EMPTY! UNIT! Dispatch_Time:EMPTY! DATETIME2 Event_Type:SKIP! " + 
              "Agency:SKIP! Agency:SKIP! Event_Sub-Type:EMPTY! CALL! Dispatch_Group:EMPTY! CH Address:EMPTY! ADDR! Location_Info:EMPTY! PLACE " + 
              "Cross_Street:EMPTY! X Municipality:EMPTY! CITY ESZ:EMPTY! MAP Development:EMPTY! MAP/L Beat:EMPTY! MAP/L " + 
              "Caller_Name:EMPTY! NAME Caller_Phone:EMPTY! PHONE Caller_Address:EMPTY! SKIP+? Event_Comments%EMPTY INFO2/N+ )");
  }
  
  @Override
  public String getFilter() {
    return "gfac55calls@gmail.com,EWFC05@verizon.net,pfdfire@fdcms.info,vfvfco168@comcast.net,westwoodfire@comcast.net,cad@oxfordfire.com,afc23@fdcms.info,whcems@gmail.com,paging@eastwhitelandfire.org,cadoxfordfire@gmail.com,haacuse96@comcast.net,libertyfc@fdcms.info,44@westwoodfire.com,dispatch@diverescue77.org,paging@honeybrookfire.org,paging89@ehbems.org,whcems@outlook.com,calls@goodfellowship.org";
  }
  
  private static HtmlDecoder decoder = new HtmlDecoder();
  
  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    int pt = body.indexOf("<style>\n");
    if (pt >= 0) body = body.substring(pt);
    if (body.startsWith("<style>")) {
      setSelectValue("2");
      String[] flds = decoder.parseHtml(body);
      if (flds == null) return false;
      if (flds[flds.length-1].endsWith("**")) {
        flds[flds.length-1] = "";
      }
      return parseFields(flds, data);
    }
    else {
      setSelectValue("1");
      body = stripFieldEnd(body, "**");
      return super.parseHtmlMsg(subject, body, data);
    }
  }
  
  private static final String MARKER_TEXT = "Chester County Emergency Services Dispatch Report";
  private static final String MARKER = MARKER_TEXT + " \n\n";

  @Override
  protected boolean parseMsg(String body, Data data) {
    int pt = body.indexOf(MARKER);
    if (pt < 0) return false;
    body = body.substring(pt+MARKER.length()).trim();
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME1")) return new DateTimeField("\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d|", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("PHONE")) return new MyPhoneField();
    if (name.equals("MARKER")) return new SkipField(MARKER_TEXT, true);
    if (name.equals("ID2")) return new MyId2Field();
    if (name.equals("DATETIME2")) return new MyDateTime2Field();
    if (name.equals("INFO2")) return new MyInfo2Field();
    return super.getField(name);
  }
  
  private class MyAddressField extends A7AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('\n');
      if (pt >= 0) {
        data.strPlace = field.substring(0,pt).trim();
        field = field.substring(pt+1).trim();
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE? " + super.getFieldNames();
    }
  }
  
  private class MyPhoneField extends PhoneField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf("x Type:");
      if (pt >= 0) {
        field = append(field.substring(0,pt).trim(), " X:", field.substring(pt+7).trim());
      }
      super.parse(field, data);
    }
  }
  
  private class MyId2Field extends IdField {
    @Override
    public void parse(String field, Data data) {
      data.strCallId = append(field, "/", data.strCallId);
    }
  }
  
  private static final Pattern DATE_TIME2_PTN = Pattern.compile("(\\d\\d)-(\\d\\d)-(\\d\\d) (\\d\\d?:\\d\\d:\\d\\d)(?: ED)?");
  private class MyDateTime2Field extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME2_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(2)+'/'+match.group(1)+'/'+match.group(3);
      data.strTime = match.group(4);
    }
  }
  
  private static final Pattern INFO_JUNK_PTN = Pattern.compile("\\d\\d?:\\d\\d:\\d\\d|[a-z]+\\d+");
  private class MyInfo2Field extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (INFO_JUNK_PTN.matcher(field).matches()) return;
      super.parse(field, data);
    }
  }
}
