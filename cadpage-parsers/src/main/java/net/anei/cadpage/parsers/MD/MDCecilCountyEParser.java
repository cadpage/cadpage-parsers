package net.anei.cadpage.parsers.MD;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class MDCecilCountyEParser extends FieldProgramParser {

  public MDCecilCountyEParser() {
    super("CECIL COUNTY", "MD",
          "( ID:ID! BOX:BOX? CALL:CODE_CALL! ( UNIT:UNIT! | ADDR:ADDR! PL:PLACE? APT:APT? UNIT:UNIT! ( INFO:INFO! CITY:CITY! ST:ST! | ) ) " +
          "| CALL:CALL! ( CODE:CODE! ADDR:ADDR! ( X:X | XST:X? ) CITY:CITY! HIGHX:X? LOWX:X? ( PL:PLACE | PLACE:PLACE | ) ( INFO:INFO! ID:ID? UNIT:UNIT! SRC:SRC? | ) DST:SKIP! " +
                       "| UNIT:UNIT! ADDR:ADDR! X:X? ID:ID? CITY:CITY? ID:ID? ST:ST? BOX:BOX? Grids:MAP? ID:ID? ST:ST? ) )");
  }

  private static final Pattern DELIM = Pattern.compile("\n|(?<!\n|^)(?=(?:ADDR|APT|BOX|CALL|CITY|CODE|DST|Grids|HIGHX|(?<!VAL)ID|INFO|LOWX|PL|PLACE|SRC|(?<!D)ST|UNIT|(?<!BO|HIGH|LO)X|XST):)"); 
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(DELIM.split(body), data);
  }

  @Override
  public String getFilter() {
    return "@c-msg.net";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    if (name.equals("APT")) return new AptField("(?:APT|RM|ROOM|SUITE|LOT)? *(.*)");
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }
  
  private static final Pattern CODE_CALL_PTN = Pattern.compile("(\\*\\*\\*PRE-ALERT\\*\\*\\* +)?(\\d{1,2}[A-Z]\\d{1,2}[A-Z]?) +(.*)");
  private class MyCodeCallField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(2);
        field = getOptGroup(match.group(1)) + match.group(3);
      }
      data.strCall = field;
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
  
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("@", "");
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }
  }
  
}
