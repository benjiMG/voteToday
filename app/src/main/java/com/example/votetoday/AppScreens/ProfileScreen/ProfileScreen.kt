package com.example.votetoday.AppScreens.ProfileScreen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.votetoday.Common.DeviceConfiguration.Companion.heightPercentage
import com.example.votetoday.Common.DeviceConfiguration.Companion.widthPercentage
import com.example.votetoday.Common.GestorBD.FBUserQuerys
import com.example.votetoday.Common.Navigation.NavigationFunctions
import com.example.votetoday.Common.SystemBarColor
import com.example.votetoday.Common.saveBitmapToFile
import com.example.votetoday.Composables.ImagePopUp
import com.example.votetoday.ui.theme.VoteTodayBackground
import com.example.votetoday.ui.theme.VoteTodayOrange


@Preview
@Composable
fun PreviewProfile() {
    ProfileScreen(navController = rememberNavController())
}


@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) {
        viewModel.updateUname()
    }
    val context = LocalContext.current


    Scaffold(
        bottomBar = { NavigationFunctions.NavBar(navController = navController as NavHostController) }
    ) {
        viewModel.updatePfp(context)
        //region Foto de perfil
        var imagenBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
        SystemBarColor(color = VoteTodayOrange)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(VoteTodayBackground)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = heightPercentage(5))
                    .height(heightPercentage(10)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                //Dialogo para cambiar la foto de perfil

                ImagePopUp(
                    pfpUrl = viewModel.fotoPerfilUrL,
                    showDialog = viewModel.dialogState,
                    defaultImage = viewModel.fotoPerfilDefault,
                    onDismissRequest = { viewModel.dialogState = false },
                    bitmap = {
                        imagenBitmap = it

                        FBUserQuerys.setFotoPerfil(saveBitmapToFile(imagenBitmap), context)
                        Log.i("ProfileScreen", "ProfileScreen: ${viewModel.fotoPerfilUrL}")
                        viewModel.dialogState = false
                        viewModel.galeryState = false
                    }
                )

                    Box(contentAlignment = Alignment.Center) {
                        Log.i("ProfileScreen", "ProfileScreen: ${viewModel.fotoPerfilUrL}")
                        AsyncImage(
                            model = if (viewModel.fotoPerfilUrL != "") viewModel.fotoPerfilUrL.also { viewModel.refresh= !viewModel.refresh;  Log.i("s", "s")  } else viewModel.fotoPerfilDefault.also { viewModel.refresh = !viewModel.refresh;  Log.i("s", "s")  },
                            contentDescription = "Foto de perfil",
                            modifier = Modifier
                                .width(widthPercentage(20))
                                .height(heightPercentage(10))
                                .clip(CircleShape)
                                .clickable(onClick = {
                                    viewModel.dialogState = true
                                }),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                            )


                    }

                //endregion
                //region Nombre de usuario
                OutlinedTextField(
                    value = viewModel.uName,
                    onValueChange = {
                        viewModel.onUnameChange(it)
                        viewModel.uName = it
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            if (viewModel.textFieldEnabled) {
                                FBUserQuerys.changeUserName(
                                    viewModel.uName,
                                    context
                                ) { nameChanged ->
                                    if (nameChanged) {
                                        viewModel.textFieldEnabled = false
                                    }
                                }
                            } else {
                                viewModel.textFieldEnabled = true
                            }
                        }) {
                            Icon(
                                painter = painterResource(viewModel.icon),
                                tint = Color.Unspecified,
                                contentDescription = "Edit icon",
                                modifier = Modifier.size(heightPercentage(4))
                            )
                        }
                    },
                    readOnly = !viewModel.textFieldEnabled,
                    singleLine = true,
                )
                //endregion


            }
            //region Lista de votaciones
            LazyColumn(
                modifier = Modifier.run {
                    width(widthPercentage(92))
                        .height(heightPercentage(93))
                        .padding(
                            top = heightPercentage(20),
                            start = widthPercentage(8)
                        )
                        .background(Color.White, RoundedCornerShape(5))
                }
            ) {

            }
            //endregion
        }
    }
}
