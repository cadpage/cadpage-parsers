package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class KSDickinsonCountyParser extends DispatchH05Parser {

  public KSDickinsonCountyParser() {
    super("DICKINSON COUNTY", "KS",
          "CFS_NUMBER? CASE_NUMBER CALL_TIME CALLER_NAME? CALLER_PHONE? ADDRESS NATURE INITIAL_DISPATCH_TIME? ( UNITS! | NARRATIVE INFO_BLK/Z+? UNITS! ) STATUS_TIMES? TIMES/Z+? NARRATIVE INFO_BLK+");
  }

  @Override
  public String getFilter() {
    return "noreply@dkcoks.org";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CFS_NUMBER")) return new SkipField("CFS NUMBER\\b *(.*)", true);
    if (name.equals("CASE_NUMBER")) return new IdField("CASE NUMBER\\b *(.*)", true);
    if (name.equals("CALL_TIME")) return new DateTimeField("CALL TIME\\b *(\\d\\d?/\\d\\d?/\\d{4} +\\d\\d?:\\d\\d:\\d\\d|)", true);
    if (name.equals("CALLER_NAME")) return new NameField("CALLER NAME\\b *(.*)", true);
    if (name.equals("CALLER_PHONE")) return new PhoneField("CALLER PHONE\\b *(.*)", true);
    if (name.equals("ADDRESS")) return new AddressCityField("ADDRESS\\b *(.*)", true);
    if (name.equals("NATURE")) return new CallField("NATURE\\b *(.*)", true);
    if (name.equals("INITIAL_DISPATCH_TIME")) return new DateTimeField("INITIAL DISPATCH TIME\\b *(\\d\\d?/\\d\\d?/\\d{4} +\\d\\d?:\\d\\d:\\d\\d|)", true);
    if (name.equals("NARRATIVE")) return new MyInfoBlockField("NARRATIVE\\b *(.*)", true);
    if (name.equals("UNITS")) return new UnitField("UNITS\\b *(.*)", true);
    if (name.equals("STATUS_TIMES")) return new MyTimesField("STATUS TIMES\\b *(.*)", true);
    return super.getField(name);
  }

  protected class MyInfoBlockField extends BaseInfoBlockField {
    protected MyInfoBlockField(String pattern, boolean hard) {
      setPattern(pattern, hard);
    }
  }

  protected class MyTimesField extends BaseTimesField {
    protected MyTimesField(String pattern, boolean hard) {
      setPattern(pattern, hard);
    }
  }
}
