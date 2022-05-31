import React from 'react';
import { SafeAreaView, View, StyleSheet, Text, TextInput, Pressable, Image } from 'react-native';
import { launchImageLibrary } from 'react-native-image-picker';

const EnterPhotoScreen = ({route, navigation}) => {

    const defaultPhoto = 'https://t4.ftcdn.net/jpg/00/64/67/63/360_F_64676383_LdbmhiNM6Ypzb3FM4PPuFP9rHe7ri8Ju.jpg'

    const [photo, updatePhoto] = React.useState({uri: defaultPhoto});

    //add image uploading function
    //change profileSource to have the image link for displaying
    const onUploadPress = () => {
        launchImageLibrary(null, (response) => {
            console.log('image picker closed');

            if (response.didCancel) {
                console.log('User cancelled image picker');
            } else if (response.error) {
                console.log('ImagePicker Error: ' + response.error);
            } else {
                console.log(response.assets[0]);
                updatePhoto(response.assets[0]);
            }
        })
    }

    //we probably don't need to do anything here, we can store image as it is added in onUploadPress
    const onPress = () => {
        navigation.navigate('EnterNameScreen', {
            email: route.params.email,
            password: route.params.password,
            gender: route.params.gender,
            birthday: route.params.birthday,
            zipcode: route.params.zipcode,
            genre: route.params.genre,
            books: route.params.books
        })
    }

    return (
        <SafeAreaView style={styles.container}>
            <Text style={styles.title}>Enter a Photo:</Text>
            <Pressable
                style={styles.button}
                onPress={onUploadPress}>
                <Text style={styles.buttonText}>Upload Photo</Text>
            </Pressable>
            <View style={styles.imageContainer}>
                <Image style={styles.image} source={photo}/>
            </View>
            <Pressable
                style={styles.button}
                onPress={onPress}>
                <Text style={styles.buttonText}>Next</Text>
             </Pressable>
        </SafeAreaView>
    );

}

const styles = StyleSheet.create({
  title: {
    color: 'black',
    fontSize: 24,
    fontWeight: 'bold',
  },
  container: {
    flex: 1,
    backgroundColor: '#fffdd3',
    alignItems: 'center',
    justifyContent: 'center',
    fontWeight: 'bold',
  },
  imageContainer: {
    paddingTop: 5,
    paddingBottom: 5,
  },
  image: {
    width: 200,
    height: 200,
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

export default EnterPhotoScreen;