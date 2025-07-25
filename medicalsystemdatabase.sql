PGDMP                      }         
   medical_db    17.4    17.4     ;           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                           false            <           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                           false            =           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                           false            >           1262    16388 
   medical_db    DATABASE     p   CREATE DATABASE medical_db WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'en-US';
    DROP DATABASE medical_db;
                     postgres    false            �            1259    16390    appointment    TABLE     �   CREATE TABLE public.appointment (
    id bigint NOT NULL,
    appointment_date_time timestamp(6) without time zone NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    doctor_id bigint NOT NULL,
    patient_id bigint NOT NULL
);
    DROP TABLE public.appointment;
       public         heap r       postgres    false            �            1259    16389    appointment_id_seq    SEQUENCE     �   ALTER TABLE public.appointment ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.appointment_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public               postgres    false    218            �            1259    16396    doctor    TABLE     �  CREATE TABLE public.doctor (
    id bigint NOT NULL,
    email character varying(255),
    experience_in_years integer NOT NULL,
    first_name character varying(255),
    gender character varying(255),
    languages_spoken character varying(255),
    last_name character varying(255),
    mobile character varying(255),
    office_address character varying(255),
    qualifications character varying(255),
    speciality character varying(255)
);
    DROP TABLE public.doctor;
       public         heap r       postgres    false            �            1259    16395    doctor_id_seq    SEQUENCE     �   ALTER TABLE public.doctor ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.doctor_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public               postgres    false    220            �            1259    16404 
   medication    TABLE     i  CREATE TABLE public.medication (
    id bigint NOT NULL,
    dosage character varying(255),
    end_date date,
    frequency character varying(255),
    medicine character varying(255),
    notes character varying(255),
    prescription_date date,
    start_date date,
    status character varying(255),
    updated_date date,
    patient_id bigint NOT NULL
);
    DROP TABLE public.medication;
       public         heap r       postgres    false            �            1259    16403    medication_id_seq    SEQUENCE     �   ALTER TABLE public.medication ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.medication_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public               postgres    false    222            �            1259    16412    patient    TABLE     	  CREATE TABLE public.patient (
    id bigint NOT NULL,
    address character varying(255),
    age integer NOT NULL,
    allergies character varying(255),
    blood_group character varying(255),
    consumes_alcohol boolean NOT NULL,
    email character varying(255),
    emergency_contact_mobile character varying(255),
    emergency_contact_name character varying(255),
    emergency_contact_relation character varying(255),
    first_name character varying(255),
    gender character varying(255),
    is_smoker boolean NOT NULL,
    last_name character varying(255),
    mobile character varying(255),
    password character varying(255),
    previous_diagnoses character varying(255),
    surgeries character varying(255),
    vaccination_history character varying(255)
);
    DROP TABLE public.patient;
       public         heap r       postgres    false            �            1259    16411    patient_id_seq    SEQUENCE     �   ALTER TABLE public.patient ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.patient_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public               postgres    false    224            2          0    16390    appointment 
   TABLE DATA           c   COPY public.appointment (id, appointment_date_time, created_at, doctor_id, patient_id) FROM stdin;
    public               postgres    false    218   �$       4          0    16396    doctor 
   TABLE DATA           �   COPY public.doctor (id, email, experience_in_years, first_name, gender, languages_spoken, last_name, mobile, office_address, qualifications, speciality) FROM stdin;
    public               postgres    false    220   %       6          0    16404 
   medication 
   TABLE DATA           �   COPY public.medication (id, dosage, end_date, frequency, medicine, notes, prescription_date, start_date, status, updated_date, patient_id) FROM stdin;
    public               postgres    false    222   �&       8          0    16412    patient 
   TABLE DATA           "  COPY public.patient (id, address, age, allergies, blood_group, consumes_alcohol, email, emergency_contact_mobile, emergency_contact_name, emergency_contact_relation, first_name, gender, is_smoker, last_name, mobile, password, previous_diagnoses, surgeries, vaccination_history) FROM stdin;
    public               postgres    false    224   P'       ?           0    0    appointment_id_seq    SEQUENCE SET     @   SELECT pg_catalog.setval('public.appointment_id_seq', 5, true);
          public               postgres    false    217            @           0    0    doctor_id_seq    SEQUENCE SET     ;   SELECT pg_catalog.setval('public.doctor_id_seq', 9, true);
          public               postgres    false    219            A           0    0    medication_id_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.medication_id_seq', 3, true);
          public               postgres    false    221            B           0    0    patient_id_seq    SEQUENCE SET     <   SELECT pg_catalog.setval('public.patient_id_seq', 4, true);
          public               postgres    false    223            �           2606    16394    appointment appointment_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.appointment
    ADD CONSTRAINT appointment_pkey PRIMARY KEY (id);
 F   ALTER TABLE ONLY public.appointment DROP CONSTRAINT appointment_pkey;
       public                 postgres    false    218            �           2606    16402    doctor doctor_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public.doctor
    ADD CONSTRAINT doctor_pkey PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.doctor DROP CONSTRAINT doctor_pkey;
       public                 postgres    false    220            �           2606    16410    medication medication_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public.medication
    ADD CONSTRAINT medication_pkey PRIMARY KEY (id);
 D   ALTER TABLE ONLY public.medication DROP CONSTRAINT medication_pkey;
       public                 postgres    false    222            �           2606    16418    patient patient_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.patient
    ADD CONSTRAINT patient_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.patient DROP CONSTRAINT patient_pkey;
       public                 postgres    false    224            �           2606    16424 '   appointment fk4apif2ewfyf14077ichee8g06    FK CONSTRAINT     �   ALTER TABLE ONLY public.appointment
    ADD CONSTRAINT fk4apif2ewfyf14077ichee8g06 FOREIGN KEY (patient_id) REFERENCES public.patient(id);
 Q   ALTER TABLE ONLY public.appointment DROP CONSTRAINT fk4apif2ewfyf14077ichee8g06;
       public               postgres    false    218    224    4764            �           2606    16419 '   appointment fkoeb98n82eph1dx43v3y2bcmsl    FK CONSTRAINT     �   ALTER TABLE ONLY public.appointment
    ADD CONSTRAINT fkoeb98n82eph1dx43v3y2bcmsl FOREIGN KEY (doctor_id) REFERENCES public.doctor(id);
 Q   ALTER TABLE ONLY public.appointment DROP CONSTRAINT fkoeb98n82eph1dx43v3y2bcmsl;
       public               postgres    false    4760    218    220            �           2606    16429 &   medication fkt1rgak6o7ydl3dm8fkja3q0pt    FK CONSTRAINT     �   ALTER TABLE ONLY public.medication
    ADD CONSTRAINT fkt1rgak6o7ydl3dm8fkja3q0pt FOREIGN KEY (patient_id) REFERENCES public.patient(id);
 P   ALTER TABLE ONLY public.medication DROP CONSTRAINT fkt1rgak6o7ydl3dm8fkja3q0pt;
       public               postgres    false    222    224    4764            2   8   x�3�4202�50�52W04�20 "������������������%�	�!W� 8�
:      4   �  x�m��N�0��'O��P����(Z� q�7Cmg������N~P�w���o�9��a���U���kk/����>�h�k�z�*�+�
Kt���QyV�HE�#v�n���~�[7�����jbh|?���5��M�i%�4�2[mV�-UF�0��(�&�E��v|�iP���v32$_ٿ��y�P?�z@"��Q��:N�,���7���)��wz�8E�'P}i}o�m&N���7�?���+9C����X�GG֚#����D�d�H�g�9���
�t5R�˳�P~�����q�,"�69|���&-�,����˻��%/� ��&������:���I�	����q�?c��-��ۢ�"�x%i;+5�9�J�q5���U�������T��"��'�)      6   x   x�3�425�M�7���4202�50�52��(�LNUHTHI��t�ͯ�L�����Sp�/M�P�,*-��W(I��J+I-R06P���+V�OSH��O��f
2δ�tL.�,KE�4����� �9%      8   m  x����r�0 г�
rK�x��Vg؜ �Lj.m[�dld�@�>b�,�VsRI�-���u4�
�#7�M��R��rP+M��Y�%�ݲ�ȿF�H@B
���L9_�C�!�rm�l4���
�� )hzFcH�J�윢5te���]����|�4O��������s��~�%���h>GY���j�>&�Ŭ�	���^�,�@]���
v,�}&
�#^�v�f�XՊg$�j��ܨh���$*op��<�����5��(#
�@�w{��%�a[��k���P,gh̏�C���1C�9�m~X����z70��M����X�Ta�3��u���κ�ۼ�ǉӾ��6�m�
�H��!�ɯ,�X�����!/�R�,@rA�i^~��� C����_����cَ���;����Cmqf9��D���#Cu����Kr���a^u�%�7mи�G���%ٺ>�M��8�M���>h������zXhN�(s�V$e���a8� )Ha8M)N�	���q����'�0{�5Z���)��5Ko<�,���M�=��Ug��U�k�Y�����ד(٢?�R�7
���b>W5&$����Hc��b^�Z��j�f�U�     