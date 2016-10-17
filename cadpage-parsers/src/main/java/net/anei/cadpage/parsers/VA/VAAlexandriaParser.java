package net.anei.cadpage.parsers.VA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class VAAlexandriaParser extends FieldProgramParser {
  
  private static final Pattern MISSING_COMMA_PTN = Pattern.compile("(?=ADDR:|APT:|X-ST:|TAC:|City:|INFO:)");
  
  public VAAlexandriaParser() {
    super(CITY_CODES, "ALEXANDRIA", "VA",
           "CALL:CALL! ADDR:ADDR/S! APT:APT! X-ST:X! UNIT:UNIT! INC#:ID! GPS:GPS! City:CITY? INFO:INFO!");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = MISSING_COMMA_PTN.matcher(body).replaceAll(";");
    body = body.replace(";INC#", ";INC#:");
    return parseFields(body.split(";"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("GPS")) return new MyGPSField();
    if (name.equals("CITY")) return new MyCityField();
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      field = p.get(" btwn ");
      data.strCross = append(p.get(" and "), " / ", p.get());
      
      p = new Parser(field);
      data.strPlace = p.getLastOptional(": @");
      String apt = p.getLastOptional(',');
      super.parse(p.get(), data);
      data.strApt = append(data.strApt, "-", apt);
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0 || field.equals("ERROR")) return;
      super.parse(field, data);
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile("(\\d{2})(\\d{6}) ,(\\d{2})(\\d{6})");
  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = GPS_PTN.matcher(field);
      if (!match.matches()) return;
      field = match.group(1)+'.' + match.group(2)+','+match.group(3)+'.'+match.group(4);
      super.parse(field,data);
    }
  }
  
  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      super.parse(field, data);
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ALEX", "ALEXANDRIA",
      "ANDL", "ANNANDALE",
      "ARLN", "ARLINGTON",
      "BRKE", "BURKE",
      "CENT", "CENTREVILLE",
      "CHAN", "CHANTILLY",
      "CLFT", "CLIFTON",
      "DLLS", "DULLES",
      "DUNN", "DUNN LORING",
      "FLCH", "FALLS CHURCH",
      "FRFX", "FAIRFAX",
      "FTBV", "FORT BELVOIR",
      "FXST", "FAIRFAX STATION",
      "GTFL", "GREAT FALLS",
      "HRND", "HERNDON",
      "LRTN", "LORTON",
      "MCLN", "MCLEAN",
      "OKTN", "OAKTON",
      "RSTN", "RESTON",
      "SFLD", "SPRINGFIELD",
      "STLG", "STERLING",
      "VNNA", "VIENNA"

  });
}
