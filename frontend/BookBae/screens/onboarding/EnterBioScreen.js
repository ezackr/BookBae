import React from 'react';
import {
  SafeAreaView,
  StyleSheet,
  Text,
  TextInput,
  Pressable,
} from 'react-native';
import Client from '../../Client';

const EnterBioScreen = ({route, navigation}) => {
  const [bio, onChangeText] = React.useState(null);

  //add necessary function to store bio
  const onPress = async () => {
    console.log('about to finish onboarding')
    console.log(route.params);
    await Client.logIn(route.params.email, route.params.password);
    const userInfo = await Client.setUserInfo({
      email: route.params.email,
      password: route.params.password,
      name: route.params.name,
      gender: route.params.gender,
      birthday: route.params.birthday,
      zipcode: route.params.zipcode,
      favGenre: route.params.genre,
      bio: bio,
    });
    console.log('books to upload');
    console.log(route.params.books);
    const bookInfo = await Client.addBooks(route.params.books);
    //console.log(userInfo);
    console.log('empty list');
    console.log(bookInfo);
    const newBookInfo = await Client.getBooks();
    console.log('new book info:');
    console.log(newBookInfo);
    navigation.navigate('TabNavigation');
  };

  return (
    <SafeAreaView style={styles.container}>
      <Text style={styles.title}>Enter a Bio (max 500 characters):</Text>
      <TextInput
        style={styles.input}
        multiline={true}
        onChangeText={onChangeText}
        value={bio}
        maxLength={500}
        placeholder="xxxxx"
      />
      <Pressable style={styles.button} onPress={onPress}>
        <Text style={styles.buttonText}>Next</Text>
      </Pressable>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  title: {
    color: 'black',
    fontSize: 24,
    fontWeight: 'bold',
  },
  input: {
    height: 40,
    width: '70%',
    margin: 1,
    borderWidth: 1,
    padding: 10,
  },
  container: {
    flex: 1,
    backgroundColor: '#fffdd3',
    alignItems: 'center',
    justifyContent: 'center',
    fontWeight: 'bold',
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

export default EnterBioScreen;
