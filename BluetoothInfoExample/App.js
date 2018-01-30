/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
  Platform,
  StyleSheet,
  Text,
  View,
  Button
} from 'react-native';

import RNBluetoothInfo from 'react-native-bluetooth-info';

const instructions = Platform.select({
  ios: 'Press Cmd+R to reload,\n' +
    'Cmd+D or shake for dev menu',
  android: 'Double tap R on your keyboard to reload,\n' +
    'Shake or press menu button for dev menu',
});

export default class App extends Component<{}> {

  constructor(props) {
    super(props);

    this.state = {
      connectionState : ''
    }
  }

  componentDidMount() {
    RNBluetoothInfo.getCurrentState().then(this.handleConnection)
  }

  componentWillMount() {
    RNBluetoothInfo.addEventListener('change', this.handleConnection);
  }

  componentWillUnmount() {
    RNBluetoothInfo.removeEventListener('change', this.handleConnection)
  }

  handleConnection = (resp) => {
    let {connectionState} = resp.type;  
    console.log('type ', connectionState);
    this.setState({connectionState});
  }

  checkBluetooth = () => {
    RNBluetoothInfo.getCurrentState().then(resp =>{ 
      let {connectionState} = resp.type;  
      this.setState({connectionState});
    } )
  }

  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>
          Welcome to React Native!
        </Text>

        <Button title="Check Bluetooth State" onPress={this.checkBluetooth} />
        <Text style={styles.instructions}>
          Cuurent Bluetooth Status: <Text style={styles.bluetoothStatusText}>{this.state.connectionState}</Text>
        </Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
  bluetoothStatusText: {
    fontSize: 16,
    fontWeight: 'bold',
    color: '#C93A3C'
  }
});
