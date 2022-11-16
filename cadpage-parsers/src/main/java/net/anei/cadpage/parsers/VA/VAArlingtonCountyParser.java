package net.anei.cadpage.parsers.VA;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class VAArlingtonCountyParser extends FieldProgramParser {

  public VAArlingtonCountyParser() {
    super(CITY_CODES, "ARLINGTON COUNTY", "VA",
          "CALL BOX? ADDR/S CITY? INC:ID Units:UNIT/S+");
  }

  @Override
  public String getFilter() {
    return "@arlingtonva.us";
  }

  private static final DateFormat DATE_TIME_FMT = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equalsIgnoreCase("Email Copy Message From Hiplink")) return false;
    if (!body.startsWith("DISPATCH:")) return false;
    body = body.substring(9).trim();

    int pt = body.indexOf('\n');
    if (pt >= 0) {
      String tail = body.substring(pt+1).trim();
      body = body.substring(0,pt).trim();
      pt = tail.lastIndexOf("  ");
      if (pt >= 0) tail = tail.substring(pt+2).trim();
      setDateTime(DATE_TIME_FMT, tail, data);
    }
    body = body.replace(" Unit:", " Units:");
    return parseFields(body.split(", "), data);
  }

  @Override
  public String getProgram() {
    return super.getProgram() + " DATE TIME";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("BOX")) return new BoxField("\\d{4,5}");
    if (name.equals("CITY")) return new MyCityField();
    return super.getField(name);
  }

  private class MyCityField extends CityField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (data.strCity.length() > 0) return false;
      return super.checkParse(field, data);
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AC",   "ARLINGTON COUNTY",
      "AX",   "ALEXANDRIA",
      "FC",   "FALLS CHURCH",
      "FX",   "FAIRFAX",
      "MW",   "RONALD REAGAN AIRPORT",

      "ALEX", "ALEXANDRIA",
      "ANDL", "ANNANDALE",
      "DUNN", "DUNN LORING",
      "FLCH", "FALLS CHURCH",
      "FRFX", "FAIRFAX",
      "MCLN", "MCLEAN"

  });
}
