import React, { useState, useEffect, useRef } from 'react'
import { BrowserRouter as Router, Routes, useNavigate, Route, Link } from "react-router-dom";
import {
  CButton,
  CCard,
  CCardBody,
  CCardGroup,
  CCol,
  CContainer,
  CForm,
  CFormInput,
  CInputGroup,
  CInputGroupText,
  CRow, CCarousel, CCarouselItem, CImage, CCarouselCaption,
  CDropdownMenu, CDropdownItem, CDropdownToggle, CDropdown,
  CWidgetStatsF, CHeader
} from '@coreui/react'
import {
  cilChartPie
} from '@coreui/icons'

import CIcon from '@coreui/icons-react'
import car1 from './imgres/car1.jpg'
import car2 from './imgres/car2.jpg'

const HomeMain = () => {

  let navigate = useNavigate();
  const nav_Login = (e, data) => {
    console.log(data)
    navigate('/login', { state: data });
    /* if(userName=='chris' && password=='chris'){
       
        dispatch(login({message:'hello chris',userName:'chris',password:password}))
        LocalStorage.store_login({userName:'chris',password:password})
     }else{
      toast.error('Invalid Credentials', { position: toast.POSITION.TOP_LEFT,autoClose:700 });
     }*/
  }


  return (
    <div className="bg-light min-vh-100 d-flex flex-row align-items-center">
      <CContainer>
        <CHeader position="sticky" className="mb-4">

          <img className='logo' src='.\TNlogo.png' alt="logo"></img><center><p className="text-primary">Government Of TamilNadu</p>
            <h1 className="text-danger">STATE SCHOLARSHIP PORTAL</h1>
            <p className="text-primary">(Academic Year 2023-2024)</p></center>

          <img className='logo2' src='.\TNlogo2.png' alt="logo"></img>
        </CHeader>
        <CCarousel controls indicators>

          <CCarouselItem>

            <CImage className="d-block w-100" src={car1} alt="slide 1" />

            <CCarouselCaption className="d-none d-md-block">

              <h5>First slide label</h5>

              <p>Some representative placeholder content for the first slide.</p>

            </CCarouselCaption>

          </CCarouselItem>

          <CCarouselItem>

            <CImage className="d-block w-100" src={car2} alt="slide 2" />

            <CCarouselCaption className="d-none d-md-block">

              <h5>Second slide label</h5>

              <p>Some representative placeholder content for the first slide.</p>

            </CCarouselCaption>

          </CCarouselItem>

          <CCarouselItem>

            <CImage className="d-block w-100" src={car1} alt="slide 3" />

            <CCarouselCaption className="d-none d-md-block">

              <h5>Third slide label</h5>

              <p>Some representative placeholder content for the first slide.</p>

            </CCarouselCaption>

          </CCarouselItem>

        </CCarousel>

        <h1>home</h1>

        <CDropdown variant="btn-group" direction="dropup">

          <CDropdownToggle color="secondary">Login</CDropdownToggle>

          <CDropdownMenu>

            <CDropdownItem onClick={(e) => { nav_Login(e, 'Student') }} >Student Login</CDropdownItem>

            <CDropdownItem onClick={(e) => { nav_Login(e, 'Institution') }} >Institution Login</CDropdownItem>

            <CDropdownItem onClick={(e) => { nav_Login(e, 'Scheme Department') }} >Scheme Departments</CDropdownItem>


            <CDropdownItem onClick={(e) => { nav_Login(e, 'Education Department') }} >Educational Departments</CDropdownItem>

          </CDropdownMenu>

        </CDropdown>

        <CRow>

          <CCol xs={6}>

            <CWidgetStatsF

              className="mb-3"

              color="primary"

              icon={<CIcon icon={cilChartPie} height={24} />}

              title="Total Beneficiaries"

              value="12678" />

          </CCol>

          <CCol xs={6}>

            <CWidgetStatsF

              className="mb-3"

              color="warning"

              icon={<CIcon icon={cilChartPie} height={24} />}

              title="Total Schemes"

              value="29" />

          </CCol>

        </CRow>

      </CContainer>
    </div>)
}

export default HomeMain