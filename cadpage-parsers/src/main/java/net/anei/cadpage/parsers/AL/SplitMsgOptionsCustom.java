package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.SplitMsgOptions;

public class SplitMsgOptionsCustom implements SplitMsgOptions {

  @Override
  public boolean splitDirectPage() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public int splitMinMsg() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public boolean splitBreakIns() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean splitBlankIns() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean splitChkSender() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean splitKeepLeadBreak() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean revMsgOrder() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean mixedMsgOrder() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean noParseSubjectFollow() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public int splitBreakLength() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int splitBreakPad() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public boolean subjectColonField() {
    // TODO Auto-generated method stub
    return false;
  }

}
