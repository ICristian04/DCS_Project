package Project.project1;

import Components.Activation;
import Components.Condition;
import Components.GuardMapping;
import Components.PetriNet;
import Components.PetriNetWindow;
import Components.PetriTransition;
import DataObjects.DataFloat;
import DataObjects.DataTransfer;
import DataOnly.TransferOperation;
import Enumerations.LogicConnector;
import Enumerations.TransitionCondition;
import Enumerations.TransitionOperation;

public class Server {
  public static void main(String[] args) {
    PetriNet pn = new PetriNet();
    pn.PetriNetName = "Server Petri";
    pn.NetworkPort = 1081;

    DataFloat p0 = new DataFloat();
    p0.SetName("p0");
    p0.SetValue(1.0f);
    pn.PlaceList.add(p0);

    DataFloat p1 = new DataFloat();
    p1.SetName("p1");
    pn.PlaceList.add(p1);

    DataFloat p2 = new DataFloat();
    p2.SetName("p2");
    pn.PlaceList.add(p2);

    DataTransfer p3 = new DataTransfer();
    p3.SetName("p3");
    p3.Value = new TransferOperation("localhost", "1080", "p5");
    pn.PlaceList.add(p3);

    // transition t1
    PetriTransition t1 = new PetriTransition(pn);
    t1.TransitionName = "t1";
    t1.InputPlaceName.add("p0");
    t1.InputPlaceName.add("p1");

    Condition T1Ct1 = new Condition(t1, "p0", TransitionCondition.NotNull);
    Condition T1Ct2 = new Condition(t1, "p1", TransitionCondition.NotNull);
    T1Ct1.SetNextCondition(LogicConnector.AND, T1Ct2);

    GuardMapping grdT1 = new GuardMapping();
    grdT1.condition = T1Ct1;

    grdT1.Activations.add(new Activation(t1, "p1", TransitionOperation.Move, "p2"));
    t1.GuardMappingList.add(grdT1);

    pn.Transitions.add(t1);

    // transition t2
    PetriTransition t2 = new PetriTransition(pn);
    t2.TransitionName = "t2";
    t2.InputPlaceName.add("p2");

    Condition T2Ct1 = new Condition(t2, "p2", TransitionCondition.NotNull);

    GuardMapping grdT2 = new GuardMapping();
    grdT2.condition = T2Ct1;

    grdT2.Activations.add(new Activation(t2, "p2", TransitionOperation.Move, "p0"));
    grdT2.Activations.add(new Activation(t2, "p2", TransitionOperation.SendOverNetwork, "p3"));
    t2.GuardMappingList.add(grdT2);

    pn.Transitions.add(t2);

    System.out.println("Exp1 started \n ------------------------------");
    pn.Delay = 3000;
    // pn.Start();

    PetriNetWindow frame = new PetriNetWindow(false);
    frame.petriNet = pn;
    frame.setVisible(true);
  }
}
