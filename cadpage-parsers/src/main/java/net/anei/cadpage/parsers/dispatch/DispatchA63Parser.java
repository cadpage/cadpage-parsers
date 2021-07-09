package net.anei.cadpage.parsers.dispatch;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.HtmlDecoder;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class DispatchA63Parser extends FieldProgramParser {

  private Properties cityCodes;

  public DispatchA63Parser(String defCity, String defState) {
    this(null, defCity, defState);
  }

  public DispatchA63Parser(Properties cityCodes, String defCity, String defState) {
    super(defCity, defState,
          "( Juris:SRC! CFS:CALL! Request#:ID! Report_Date/Time:DATETIME! Reporting_Period:SKIP! Location:ADDR! Notify_Type:SKIP Call#:SKIP Login_User:SKIP " +
          "| CFS:CALL! Location:ADDR! Call#:ID! Units_Dispatched:UNIT! Stations_Dispatched:SRC? Report_Date/Time:DATETIME! ) Comments:INFO/N+");
    this.cityCodes = cityCodes;
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (body.startsWith("<html>")) {
      String[] flds = rebuildAlert(body);
      if (flds == null) return false;
      return parseFields(flds, data);
    } else {
      return super.parseHtmlMsg(subject, body, data);
    }
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace("\t", "");
    return parseFields(body.split("\n"), data);
  }

  private HtmlDecoder decoder = null;

  private String[] rebuildAlert(String body) {

    // If we haven't build an HTML decoder yet, do it now
    if (decoder == null) {
      decoder = new HtmlDecoder("tr");
    }

    // And use it to parse the html fields
    String[] flds = decoder.parseHtml(body);

    // Before we return this, we need to compress all of the fields in a row into one field
    List<String> result = new ArrayList<>();
    StringBuilder sb = null;
    for (String fld : flds) {
      if (fld.equals("<|tr|>")) {
        if (sb != null) return null;
        sb = new StringBuilder();
      }
      else if (fld.equals("<|/tr|>")) {
        if (sb == null) return null;
        result.add(sb.toString());
        sb = null;
      }
      else if (sb != null) {
        sb.append(fld);
      } else {
        result.add(fld);
      }
    }

    if (sb != null) result.add(sb.toString());

    return result.toArray(new String[0]);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{2}\\-\\d{6}", true);
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }

  private static final SimpleDateFormat MY_DATE_TIME_FMT
    = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      setDateTime(MY_DATE_TIME_FMT, field, data);
    }
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String city = p.getLastOptional(';');
      if (cityCodes != null) city = convertCodes(city.toUpperCase(), cityCodes);
      data.strCity = city;
      String apt = p.getLastOptional(',');
      if (apt.equals("BLDG") || apt.equals("LOT") || apt.equals("APT")) apt = "";
      super.parse(p.get(), data);
      data.strApt = append(data.strApt, "-", apt);
      return;
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT CITY";
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      data.strUnit = field.replace(" ", "");
    }
  }

}
