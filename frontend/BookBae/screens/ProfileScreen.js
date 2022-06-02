import React from 'react';
import {StyleSheet, Text, SafeAreaView, View, Image, Pressable} from 'react-native';
import Client from '../Client.js';
import { AgeFromDate } from 'age-calculator';

const ProfileScreen = () => {

  const [date, setDate] = React.useState(new Date());
  const [profile, setProfile] = React.useState([]);

  const defaultPhoto = 'https://t4.ftcdn.net/jpg/00/64/67/63/360_F_64676383_LdbmhiNM6Ypzb3FM4PPuFP9rHe7ri8Ju.jpg'
  const [photo, updatePhoto] = React.useState({uri: defaultPhoto});

  const getProfileData = async () => {
    Client.getUserInfo().then(data => {
      if (data.length != 0) {
        setProfile({name: data.name, birthday: data.birthday, bio: data.bio});
        setDate(new Date(data.birthday));
      }
    });
  };

  React.useEffect(() => {
    getProfileData();
  });

  return (
    <SafeAreaView style={styles.container}>
      <Text style={styles.title}>{profile.name}, {new AgeFromDate(date).age}</Text>
      <View style={styles.imageContainer}>
        <Image style={styles.image} source={photo}/>
      </View>
      <Text>{profile.bio}</Text>

    <SafeAreaView style={styles.bookDisplay}>
      <Image
        style={styles.book}
        source={require('../images/title2.jpeg')}
      />
      <Image
        style={styles.book}
        source={require('../images/title4.jpeg')}
      />
    </SafeAreaView>
    </SafeAreaView>
  );
};

export default ProfileScreen;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fffdd3',
    alignItems: 'center',
    justifyContent: 'center',
    fontWeight: 'bold',
  },
  title: {
    color: 'black',
    fontSize: 32,
    fontWeight: 'bold',
    padding: 10,
  },
  imageContainer: {
    paddingTop: 5,
    paddingBottom: 5,
  },
  image: {
    width: 200,
    height: 200,
  },
  bookDisplay: {
    flex: 3,
    flexDirection: 'row',
    justifyContent: 'space-evenly',
    alignItems: 'flex-end',
    padding: 5,
  },
  // books in book display
  book: {
    height: 175,
    width: 125,
    margin: 10,
    borderRadius: 10,
  },
  button: {
      alignItems: 'center',
      justifyContent: 'center',
      paddingVertical: 12,
      paddingHorizontal: 32,
      borderRadius: 4,
      elevation: 3,
      backgroundColor: '#BD2A2A',
      marginTop: 5,
      marginBottom: 5,
  },
  buttonText: {
      fontSize: 16,
      lineHeight: 21,
      fontWeight: 'bold',
      letterSpacing: 0.25,
      color: 'white',
  },
});
