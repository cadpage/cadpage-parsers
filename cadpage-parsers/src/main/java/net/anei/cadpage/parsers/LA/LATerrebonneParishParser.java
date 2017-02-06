package net.anei.cadpage.parsers.LA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.HtmlDecoder;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA13Parser;

public class LATerrebonneParishParser extends DispatchA13Parser {
  
  private Field addrField;
  private HtmlDecoder htmlDecoder = new HtmlDecoder();

  public LATerrebonneParishParser() {
    super("TERREBONNE PARISH", "LA");
    addrField = getField("ADDR");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
    setupProtectedNames("J AND V GUIDRY");
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
    if (name.equals("ADDR")) return new MyAddressField(false);
    return super.getField(name);
  }
  
  private class MyAddressField extends BaseAddressField {
    
    public MyAddressField(boolean includeCall) {
      super(includeCall);
    }

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
  
  private static final String[] MWORD_STREET_LIST = new String[]{
    "AFTON VILLA",
    "ALS TRAILER",
    "ASHLAND LANDFILL",
    "BACK PROJECT",
    "BAYOU BLACK",
    "BAYOU BLUE BY PASS",
    "BAYOU DULARGE",
    "BAYOU GARDENS",
    "BONNIE BLUE",
    "CEDER GROVE",
    "CYPRESS VILLAGE",
    "DAIGLES LAKE",
    "DR BEATROUS",
    "FALGOUT CANAL",
    "GARDEN VIEW",
    "GRAND CAILLOU",
    "GULF ACCESS",
    "HENRY CLAY",
    "JERRY ANN",
    "JESSICA LYNN",
    "JOHN EDWARD",
    "JOHNSON RIDGE",
    "LILEY PORCHE",
    "LINDA LEE",
    "LUMEN CHRISTI",
    "MAIN PROJECT",
    "MARTIN LUTHER KING",
    "MARY KAY",
    "MAW MAW",
    "NORTH BAYOU BLACK",
    "OAK POINTE",
    "PORT AU PRINCE",
    "SAINT CHARLES",
    "SAINT MATT",
    "SHADY ARBORS",
    "SHADY OAK",
    "SOUTH BAYOU BLACK",
    "SOUTH FRENCH QUARTER",
    "SOUTH HOLLYWOOD",
    "SOUTH LEGION",
    "SOUTHDOWN MANDALAY",
    "SUGAR BEND",
    "SUGAR CREEK",
    "SUGAR LAND",
    "TERRA CANE",
    "THERIOT VOISIN BRIDGE",
    "TWELVE OAKS",
    "WEST MAIN",
    "WEST PARK",
    "WEST TUNNEL",
    "WINTER QUARTERS"
  };
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "111 FIRE-STRUCTURE",
      "143 FIRE-GRASS",
      "321 MEDICAL EMERGENCY",
      "322 ACCIDENT W/INJURY",
      "324 ACCIDENT W/O INJURY",
      "400 HAZARDOUS CONDITION",
      "444 POWER LINE DOWN",
      "445(I) ARCING INSIDE",
      "445(O) ARCING OUTSIDE",
      "461 BUILDING OR STRUCTURE WEAKEND OR COLLASPED",
      "500 SERVICE CALL",
      "531(I) SMOKE ODOR INSIDE",
      "745 ALARM-FIRE",
      "746 ALARM-CARBON MONOXIDE",
      "XFER TPSO"
 );
}
