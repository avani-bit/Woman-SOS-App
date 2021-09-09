import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:flutter/services.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.red,
      ),
      home: MyHomePage(),
    );
  }
}

class MyHomePage extends StatefulWidget {
  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  static const methodChannel = const MethodChannel("backgroundService");

  Future<void> startServiceInPlatform() async {
    if (Platform.isAndroid) {
      //String data =
      try {
        var result = await methodChannel.invokeMethod("start");
        print("res=" + result);
      } on Exception catch (e) {
        print(e);
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    var names = ["Contact1", "Contact2", "Contact3"];
    var contactNo = ["1234567890", "0987654321", "8349994449"];
    return Scaffold(
        appBar: AppBar(
          title: Text("Your Safety First"),
        ),
        body: Column(children: [
          SizedBox(height: 10.0),
          ElevatedButton(
            child: Text("Start Your Safety App"),
            onPressed: () {
              startServiceInPlatform();
            },
            style: ElevatedButton.styleFrom(
                primary: Colors.red,
                padding: EdgeInsets.symmetric(horizontal: 80, vertical: 30),
                textStyle:
                    TextStyle(fontSize: 20, fontWeight: FontWeight.bold)),
          ),
          SizedBox(height: 10.0),
          ListView.builder(
            itemCount: 3,
            shrinkWrap: true,
            itemBuilder: (BuildContext context, int index) => Container(
              width: MediaQuery.of(context).size.width,
              padding: EdgeInsets.symmetric(horizontal: 10.0, vertical: 10.0),
              child: Card(
                  elevation: 5.0,
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(0.0),
                  ),
                  child: Container(
                    width: MediaQuery.of(context).size.width,
                    padding:
                        EdgeInsets.symmetric(horizontal: 10.0, vertical: 10.0),
                    child: Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        crossAxisAlignment: CrossAxisAlignment.center,
                        children: <Widget>[
                          Row(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            mainAxisAlignment: MainAxisAlignment.start,
                            children: <Widget>[
                              Container(
                                width: 55.0,
                                height: 55.0,
                                child: CircleAvatar(
                                  backgroundColor: Colors.red,
                                  foregroundColor: Colors.red,
                                  //backgroundImage: NetworkImage("data:image/jpeg;base64,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"),
                                ),
                              ),
                              SizedBox(width: 6.8),
                              Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: <Widget>[
                                  Text(names[index],
                                      style: TextStyle(
                                          color: Colors.black,
                                          fontSize: 18.0,
                                          fontWeight: FontWeight.bold)),
                                  SizedBox(height: 5.0),
                                  Text(contactNo[index],
                                      style: TextStyle(color: Colors.grey)),
                                ],
                              ),
                            ],
                          ),
                          Container(
                            alignment: Alignment.center,
                            padding: EdgeInsets.symmetric(
                                horizontal: 10.8, vertical: 10.8),
                            child: FlatButton(
                              onPressed: () {},
                              color: Colors.redAccent,
                              shape: RoundedRectangleBorder(
                                borderRadius: BorderRadius.circular(20.0),
                              ),
                              child: Text("Edit",
                                  style: TextStyle(color: Colors.white)),
                            ),
                          )
                        ]),
                  )),
            ),
          )
        ]));
  }
}
