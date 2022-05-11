import React from 'react';
import {
  StyleSheet,
  Text,
  SafeAreaView,
  Pressable,
  Image,
  TouchableOpacity,
} from 'react-native';

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
        <TouchableOpacity style={styles.button} activeOpacity={0.5}>
          <Image source={require('../images/deny.png')} />
        </TouchableOpacity>
        <TouchableOpacity style={styles.button} activeOpacity={0.5}>
          <Image source={require('../images/accept.png')} />
        </TouchableOpacity>
      </SafeAreaView>
    </SafeAreaView>
  );
};

export default HomeScreen;

const ProfileOverview = () => {
  return (
    <SafeAreaView style={matchStyles.matchBox}>
      <Text style={matchStyles.frontName}>Alan, 29</Text>
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
    flexDirection: 'row',
    justifyContent: 'center',
  },
  matchMenu: {
    flex: 6,
    justifyContent: 'center',
    alignItems: 'center',
  },
  bottomMenu: {
    flex: 1,
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    paddingBottom: 20,
  },
  title: {
    color: 'black',
    fontSize: 48,
    fontWeight: 'bold',
    paddingTop: 20,
  },
  button: {
    height: 50,
    margin: 10,
  },
});

const matchStyles = StyleSheet.create({
  matchBox: {
    height: 500,
    width: 300,
    backgroundColor: '#ffe9a1',
    justifyContent: 'center',
    alignItems: 'center',
  },
  frontName: {
    color: 'black',
    fontSize: 24,
    fontWeight: 'bold',
    justifyContent: 'center',
    alignItems: 'center',
  },
});
