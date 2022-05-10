import React from 'react';
import {StyleSheet, Text, SafeAreaView, Pressable, Image} from 'react-native';

const HomeScreen = ({navigation}) => {
  return (
    <SafeAreaView style={styles.container}>
      <SafeAreaView style={styles.topMenu}>
        <Text style={styles.title}>BookBae</Text>
      </SafeAreaView>
      <SafeAreaView style={styles.matchMenu}>
        <ProfileOverview />
      </SafeAreaView>
      <SafeAreaView style={styles.bottomMenu}>
        <Pressable style={styles.button}>
          <Text>No</Text>
        </Pressable>
        <Image source={require('../images/deny.png')} />
      </SafeAreaView>
    </SafeAreaView>
  );
};

export default HomeScreen;

const ProfileOverview = () => {
  return (
    <SafeAreaView style={matchStyles.match}>
      <Text>Hello World</Text>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fffdd3',
    alignItems: 'center',
    justifyContent: 'center',
  },
  topMenu: {
    flex: 1,
    backgroundColor: 'red',
    flexDirection: 'row',
    justifyContent: 'center',
  },
  matchMenu: {
    flex: 5,
    backgroundColor: 'green',
  },
  bottomMenu: {
    flex: 1,
    backgroundColor: 'blue',
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
  },
  title: {
    color: 'black',
    fontSize: 30,
    fontWeight: 'bold',
  },
  button: {
    color: 'white',
    margin: 10,
  },
});

const matchStyles = StyleSheet.create({
  match: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
});
